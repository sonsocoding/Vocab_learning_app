package com.example.vocablearningapp.data.ai

import com.example.vocablearningapp.domain.model.PartOfSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class GeminiApiClient(private val preferences: AiPreferences) {

    private val systemPrompt = """
        You are "VocabAI Coach", an expert English learning AI Tutor inside VocabLearningApp.
        Your goal is to help users learn English, answer language questions, and generate high-quality vocabulary sets.

        You MUST ALWAYS return a valid JSON object matching one of these two structures:

        1. When the user asks to CREATE A SET, GENERATE VOCABULARY, or PRACTICE A TOPIC:
        {
          "message": "Friendly response and explanation of the topic to the user",
          "action": "CREATE_VOCAB_SET",
          "data": {
            "title": "Short Title (e.g. Job Interview Mastery)",
            "description": "Brief description of the set",
            "level": "A1" | "A2" | "B1" | "B2" | "C1" | "C2",
            "category": "General" | "Work" | "Travel" | "Academic" | "Interests" | "Education" | "Society" | "Everyday",
            "words": [
              {
                "word": "negotiate",
                "ipa": "/nɪˈɡəʊʃieɪt/",
                "partOfSpeech": "verb",
                "meaning": "đàm phán, thương lượng",
                "exampleSentence": "They managed to negotiate a better contract."
              }
            ]
          }
        }

        2. When the user asks a GENERAL QUESTION, GRAMMAR QUERY, or CONVERSATIONAL CHAT:
        {
          "message": "Your clear, markdown-formatted explanation here",
          "action": "NONE"
        }

        Rules:
        - The `meaning` of each word must always be in Vietnamese.
        - The `exampleSentence` must be natural English demonstrating the word in clear context.
        - Return strictly valid raw JSON with no Markdown code block wrapping.
    """.trimIndent()

    suspend fun sendMessage(
        userPrompt: String,
        history: List<AiChatMessage> = emptyList()
    ): Result<AiChatMessage> = withContext(Dispatchers.IO) {
        val apiKey = preferences.apiKey.trim()
        if (apiKey.isBlank()) {
            // Offline / Demo Mock Engine when no API key is provided
            return@withContext Result.success(getMockResponse(userPrompt))
        }

        try {
            val modelName = preferences.model.ifBlank { "gemini-1.5-flash" }
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("Accept", "application/json")
                doOutput = true
                connectTimeout = 15000
                readTimeout = 25000
            }

            // Build strictly valid alternating contents
            val contentsArray = JSONArray()
            var lastRole: String? = null

            // Take last few messages, ensuring first is "user" and they alternate
            val sanitizedHistory = history.takeLast(8)
            for (msg in sanitizedHistory) {
                val role = if (msg.sender == MessageSender.USER) "user" else "model"
                if (lastRole == null && role != "user") continue // First turn must be user
                if (role == lastRole) continue // Must alternate
                val contentObj = JSONObject().apply {
                    put("role", role)
                    val partsArray = JSONArray().apply {
                        put(JSONObject().put("text", msg.text))
                    }
                    put("parts", partsArray)
                }
                contentsArray.put(contentObj)
                lastRole = role
            }

            // Append current user prompt
            contentsArray.put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
                }
            )

            val requestBody = JSONObject().apply {
                put("contents", contentsArray)

                // System Instruction (both standard formats)
                put(
                    "systemInstruction",
                    JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                    }
                )

                // Generation Config
                put(
                    "generationConfig",
                    JSONObject().apply {
                        put("temperature", 0.7)
                        put("responseMimeType", "application/json")
                    }
                )
            }

            OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use { writer ->
                writer.write(requestBody.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val responseText = BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)).use { it.readText() }
                val parsedMessage = parseGeminiResponse(responseText)
                Result.success(parsedMessage)
            } else {
                val errorStream = connection.errorStream
                val errorText = if (errorStream != null) {
                    BufferedReader(InputStreamReader(errorStream, StandardCharsets.UTF_8)).use { it.readText() }
                } else "HTTP Error $responseCode"

                // Extract human-readable error from Google error JSON if present
                val readableError = try {
                    val errJson = JSONObject(errorText)
                    errJson.optJSONObject("error")?.optString("message", errorText) ?: errorText
                } catch (e: Exception) {
                    errorText
                }

                Result.success(
                    AiChatMessage(
                        sender = MessageSender.AI,
                        text = "⚠️ **Gemini API Error ($responseCode)**:\n$readableError\n\n*(Please check your API key in Settings 🔑 or ensure your Google AI Studio project is active)*"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.success(
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "⚠️ **Connection Error**: ${e.localizedMessage ?: "Could not connect to Gemini API"}\n\n*(Please check your internet connection or API Key)*"
                )
            )
        }
    }

    private fun parseGeminiResponse(rawJson: String): AiChatMessage {
        val root = JSONObject(rawJson)
        val candidates = root.optJSONArray("candidates")
        val firstCandidate = candidates?.optJSONObject(0)
        val content = firstCandidate?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        val rawText = parts?.optJSONObject(0)?.optString("text", "{}") ?: "{}"

        var cleanedText = rawText.trim()
        if (cleanedText.startsWith("```json")) {
            cleanedText = cleanedText.removePrefix("```json").trim()
        }
        if (cleanedText.startsWith("```")) {
            cleanedText = cleanedText.removePrefix("```").trim()
        }
        if (cleanedText.endsWith("```")) {
            cleanedText = cleanedText.removeSuffix("```").trim()
        }

        return try {
            val jsonObject = JSONObject(cleanedText)
            val messageText = jsonObject.optString("message", "Here is the result:")
            val action = jsonObject.optString("action", "NONE")

            var actionPayload: AiActionPayload? = null
            if (action.equals("CREATE_VOCAB_SET", ignoreCase = true)) {
                val dataObj = jsonObject.optJSONObject("data")
                if (dataObj != null) {
                    val title = dataObj.optString("title", "AI Generated Set")
                    val description = dataObj.optString("description", "Custom set created by VocabAI Coach")
                    val level = dataObj.optString("level", "B1")
                    val category = dataObj.optString("category", "General")
                    val wordsArray = dataObj.optJSONArray("words") ?: JSONArray()

                    val words = mutableListOf<AiGeneratedWord>()
                    for (i in 0 until wordsArray.length()) {
                        val wObj = wordsArray.optJSONObject(i) ?: continue
                        val word = wObj.optString("word", "")
                        val ipa = wObj.optString("ipa", "/${word}/")
                        val posStr = wObj.optString("partOfSpeech", "noun")
                        val pos = when (posStr.lowercase()) {
                            "verb", "v" -> PartOfSpeech.VERB
                            "adj", "adjective" -> PartOfSpeech.ADJECTIVE
                            else -> PartOfSpeech.NOUN
                        }
                        val meaning = wObj.optString("meaning", "")
                        val example = wObj.optString("exampleSentence", "")
                        if (word.isNotBlank()) {
                            words.add(AiGeneratedWord(word, ipa, pos, meaning, example))
                        }
                    }

                    if (words.isNotEmpty()) {
                        val generatedSet = AiGeneratedSet(title, description, level, category, words)
                        actionPayload = AiActionPayload.CreateSetPayload(generatedSet)
                    }
                }
            }

            AiChatMessage(
                sender = MessageSender.AI,
                text = messageText,
                actionPayload = actionPayload
            )
        } catch (e: Exception) {
            AiChatMessage(
                sender = MessageSender.AI,
                text = rawText
            )
        }
    }

    private fun getMockResponse(prompt: String): AiChatMessage {
        val lower = prompt.lowercase()

        return when {
            lower.contains("interview") || lower.contains("phỏng vấn") || lower.contains("job") || lower.contains("xin việc") -> {
                val words = listOf(
                    AiGeneratedWord("competence", "/ˈkɒmpɪtəns/", PartOfSpeech.NOUN, "năng lực; khả năng thành thạo", "She demonstrated great competence in project management."),
                    AiGeneratedWord("negotiate", "/nɪˈɡəʊʃieɪt/", PartOfSpeech.VERB, "thương lượng, đàm phán", "He tried to negotiate a higher salary during the interview."),
                    AiGeneratedWord("collaborative", "/kəˈlæbərətɪv/", PartOfSpeech.ADJECTIVE, "có tính hợp tác; phối hợp", "We foster a collaborative work environment across all departments."),
                    AiGeneratedWord("proficient", "/prəˈfɪʃnt/", PartOfSpeech.ADJECTIVE, "thành thạo, điêu luyện", "She is highly proficient in modern Kotlin and Android development."),
                    AiGeneratedWord("initiative", "/ɪˈnɪʃətɪv/", PartOfSpeech.NOUN, "sáng kiến; sự chủ động", "He took the initiative to improve the onboarding process.")
                )
                val set = AiGeneratedSet(
                    title = "Job Interview Mastery",
                    description = "Key vocabulary and phrases for job interviews",
                    level = "B2",
                    category = "Work",
                    words = words
                )
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "Here is a curated vocabulary set for **Job Interviews (B2 Level)**. Review the words below and tap **Save to My Sets** to start practicing with Flashcards & Quizzes!",
                    actionPayload = AiActionPayload.CreateSetPayload(set)
                )
            }

            lower.contains("travel") || lower.contains("du lịch") || lower.contains("airport") || lower.contains("sân bay") -> {
                val words = listOf(
                    AiGeneratedWord("itinerary", "/aɪˈtɪnərəri/", PartOfSpeech.NOUN, "lịch trình chuyến đi", "We planned a detailed itinerary for our 5-day trip to Tokyo."),
                    AiGeneratedWord("embark", "/ɪmˈbɑːk/", PartOfSpeech.VERB, "lên tàu; bắt đầu một hành trình", "Passengers will embark on the cruise ship tomorrow morning."),
                    AiGeneratedWord("picturesque", "/ˌpɪktʃəˈresk/", PartOfSpeech.ADJECTIVE, "đẹp như tranh vẽ", "They stayed in a picturesque mountain village near the lake."),
                    AiGeneratedWord("hospitality", "/ˌhɒspɪˈtæləti/", PartOfSpeech.NOUN, "lòng hiếu khách", "The local residents welcomed us with warm hospitality."),
                    AiGeneratedWord("souvenir", "/ˌsuːvəˈnɪə(r)/", PartOfSpeech.NOUN, "quà lưu niệm", "I bought a handmade souvenir from the traditional market.")
                )
                val set = AiGeneratedSet(
                    title = "Travel & Wanderlust Essentials",
                    description = "Must-know words for traveling and exploring the world",
                    level = "B1",
                    category = "Travel",
                    words = words
                )
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "Here is an essential vocabulary set for **Travel & Tourism (B1 Level)**. Tap **Save to My Sets** to practice immediately!",
                    actionPayload = AiActionPayload.CreateSetPayload(set)
                )
            }

            lower.contains("affect") || lower.contains("effect") -> {
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = """
                        ### 🔍 Phân biệt "Affect" và "Effect":
                        
                        1. **Affect (thường là Động từ - Verb):** Có nghĩa là *tác động, ảnh hưởng đến cái gì*.
                           - *Ví dụ:* "Smoking will **affect** your health negatively." (Hút thuốc sẽ ảnh hưởng xấu tới sức khỏe bạn).
                           
                        2. **Effect (thường là Danh từ - Noun):** Có nghĩa là *kết quả, tác động, hiệu ứng*.
                           - *Ví dụ:* "The positive **effect** of regular exercise is undeniable." (Tác động tích cực của việc tập thể dục là không thể phủ nhận).
                           
                        > 💡 **Mẹo ghi nhớ (RAVEN):**  
                        > **R**emember: **A**ffect = **V**erb, **E**ffect = **N**oun.
                    """.trimIndent()
                )
            }

            lower.contains("fsrs") || lower.contains("thuật toán") || lower.contains("spaced repetition") -> {
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = """
                        ### 🧠 Thuật toán FSRS-6 trong VocabLearningApp:
                        
                        FSRS-6 (Free Spaced Repetition Scheduler v6) là thuật toán hiện đại mô phỏng trí nhớ não bộ dựa trên 3 thông số:
                        
                        - **Retrievability (R):** Xác suất bạn nhớ lại từ sau một khoảng thời gian.
                        - **Stability (S):** Số ngày cần thiết để xác suất nhớ giảm xuống 90%.
                        - **Difficulty (D):** Độ khó nội tại của từ đối với não bộ bạn (1 - 10).
                        
                        Ứng dụng tự động chuyển đổi phản xạ làm bài trong 6 Game thành 4 mức đánh giá: `Again`, `Hard`, `Good`, `Easy` để tối ưu hóa thời gian học tập của bạn!
                    """.trimIndent()
                )
            }

            else -> {
                val words = listOf(
                    AiGeneratedWord("adaptable", "/əˈdæptəbl/", PartOfSpeech.ADJECTIVE, "dễ thích nghi, linh hoạt", "Successful professionals are adaptable to fast-changing environments."),
                    AiGeneratedWord("persevere", "/ˌpɜːsɪˈvɪə(r)/", PartOfSpeech.VERB, "kiên trì, bền bỉ", "If you persevere through challenges, you will achieve your goal."),
                    AiGeneratedWord("perspective", "/pəˈspektɪv/", PartOfSpeech.NOUN, "góc nhìn; quan điểm", "Studying abroad gives you a broader perspective on life."),
                    AiGeneratedWord("diligent", "/ˈdɪlɪdʒənt/", PartOfSpeech.ADJECTIVE, "siêng năng, cần cù", "Her diligent practice helped her master advanced English quickly."),
                    AiGeneratedWord("milestone", "/ˈmaɪlstəʊn/", PartOfSpeech.NOUN, "cột mốc quan trọng", "Reaching a 30-day learning streak is an awesome milestone!")
                )
                val set = AiGeneratedSet(
                    title = "AI Daily Growth Vocabulary",
                    description = "Inspiring words generated specifically for your learning journey",
                    level = "B2",
                    category = "Education",
                    words = words
                )
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "I've generated a special custom set for you: **AI Daily Growth Vocabulary**! You can save this set directly to your local library and start practicing with FSRS-6 spaced repetition.",
                    actionPayload = AiActionPayload.CreateSetPayload(set)
                )
            }
        }
    }
}
