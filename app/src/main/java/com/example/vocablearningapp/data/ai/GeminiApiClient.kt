package com.example.vocablearningapp.data.ai

import android.util.Log
import com.example.vocablearningapp.domain.model.PartOfSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GeminiApiClient(private val preferences: AiPreferences) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

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
            return@withContext Result.success(getMockResponse(userPrompt))
        }

        try {
            // Step 1: Determine list of candidate models to try
            val candidateModels = getCandidateModels(apiKey)

            var lastErrorMessage: String? = null
            var lastErrorCode: Int = 0

            for (model in candidateModels) {
                val cleanModel = model.removePrefix("models/")
                val responseResult = tryModel(cleanModel, apiKey, userPrompt, history)

                if (responseResult.isSuccess) {
                    // Cache the successful model
                    preferences.model = cleanModel
                    return@withContext Result.success(responseResult.getOrThrow())
                } else {
                    val err = responseResult.exceptionOrNull()
                    lastErrorMessage = err?.message ?: "Unknown error"
                    if (lastErrorMessage.contains("HTTP 404") || lastErrorMessage.contains("not found") || lastErrorMessage.contains("not supported")) {
                        Log.w("GeminiApiClient", "Model $cleanModel failed (404/not supported). Trying next candidate...")
                        continue
                    } else if (lastErrorMessage.contains("API_KEY_INVALID") || lastErrorMessage.contains("HTTP 400") && lastErrorMessage.contains("API key not valid")) {
                        // Immediate exit if key is invalid
                        return@withContext Result.success(
                            AiChatMessage(
                                sender = MessageSender.AI,
                                text = "⚠️ **API Key Invalid (400/403)**:\nGoogle Gemini báo API Key không hợp lệ. Vui lòng kiểm tra lại Key trên aistudio.google.com và dán lại vào Cài đặt 🔑."
                            )
                        )
                    }
                }
            }

            // If all candidate models failed
            Result.success(
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "⚠️ **Không tìm thấy Model Gemini khả dụng trên API Key này**:\n${lastErrorMessage ?: "Vui lòng kiểm tra lại quyền của API Key"}\n\n💡 Bạn hãy kiểm tra xem API key đã được kích hoạt dịch vụ Gemini API tại aistudio.google.com chưa nhé."
                )
            )
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            Result.success(
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "⚠️ **Không có Internet trên máy ảo (No Internet)**:\nKhông thể kết nối đến máy chủ Google Gemini.\n\n💡 **Cách khắc phục nhanh:**\n1. Trong Android Studio: Mở tab **Device Manager** -> bấm dấu 3 chấm `⋮` cạnh máy ảo -> chọn **Cold Boot Now**.\n2. Hoặc vào Settings máy ảo -> Network & internet -> Internet -> AndroidWifi -> Đổi DNS sang `8.8.8.8`."
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.success(
                AiChatMessage(
                    sender = MessageSender.AI,
                    text = "⚠️ **Lỗi kết nối**: ${e.javaClass.simpleName}: ${e.localizedMessage ?: "Không thể kết nối API"}\n\n*(Vui lòng kiểm tra mạng máy ảo hoặc API key)*"
                )
            )
        }
    }

    private suspend fun getCandidateModels(apiKey: String): List<String> = withContext(Dispatchers.IO) {
        val defaultFallbackList = listOf(
            "gemini-2.5-flash",
            "gemini-2.0-flash",
            "gemini-2.0-flash-exp",
            "gemini-1.5-flash-8b",
            "gemini-1.5-flash",
            "gemini-1.5-flash-002",
            "gemini-1.5-flash-001",
            "gemini-1.5-pro",
            "gemini-pro"
        )

        // If user already selected a valid model preference, put it first
        val selected = preferences.model.trim().removePrefix("models/")
        val orderedList = mutableListOf<String>()
        if (selected.isNotBlank() && !defaultFallbackList.contains(selected)) {
            orderedList.add(selected)
        }

        // Try dynamically querying Google Gemini models.list endpoint
        try {
            val listUrl = "https://generativelanguage.googleapis.com/v1beta/models?key=$apiKey"
            val request = Request.Builder()
                .url(listUrl)
                .get()
                .addHeader("Accept", "application/json")
                .build()

            val response = httpClient.newCall(request).execute()
            val body = response.body?.string().orEmpty()

            if (response.isSuccessful && body.isNotBlank()) {
                val json = JSONObject(body)
                val modelsArray = json.optJSONArray("models")
                if (modelsArray != null && modelsArray.length() > 0) {
                    val availableSupported = mutableListOf<String>()
                    for (i in 0 until modelsArray.length()) {
                        val mObj = modelsArray.optJSONObject(i) ?: continue
                        val name = mObj.optString("name", "").removePrefix("models/")
                        val supportedMethods = mObj.optJSONArray("supportedGenerationMethods")
                        var supportsGenerate = false
                        if (supportedMethods != null) {
                            for (j in 0 until supportedMethods.length()) {
                                if (supportedMethods.optString(j) == "generateContent") {
                                    supportsGenerate = true
                                    break
                                }
                            }
                        }
                        if (supportsGenerate && name.isNotBlank()) {
                            availableSupported.add(name)
                        }
                    }

                    if (availableSupported.isNotEmpty()) {
                        // Sort: prefer flash models over pro/experimental
                        val prioritized = availableSupported.sortedWith(compareByDescending<String> {
                            when {
                                it.contains("2.5-flash") -> 100
                                it.contains("2.0-flash") -> 90
                                it.contains("1.5-flash") -> 80
                                it.contains("flash") -> 70
                                it.contains("pro") -> 60
                                else -> 50
                            }
                        })
                        return@withContext (orderedList + prioritized).distinct()
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("GeminiApiClient", "Failed to query dynamic models list: ${e.localizedMessage}")
        }

        return@withContext (orderedList + defaultFallbackList).distinct()
    }

    private fun tryModel(
        modelName: String,
        apiKey: String,
        userPrompt: String,
        history: List<AiChatMessage>
    ): Result<AiChatMessage> {
        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

        val contentsArray = JSONArray()
        var lastRole: String? = null

        val sanitizedHistory = history.takeLast(6)
        for (msg in sanitizedHistory) {
            val role = if (msg.sender == MessageSender.USER) "user" else "model"
            if (lastRole == null && role != "user") continue
            if (role == lastRole) continue
            val partObj = JSONObject().put("text", msg.text)
            contentsArray.put(JSONObject().apply {
                put("role", role)
                put("parts", JSONArray().put(partObj))
            })
            lastRole = role
        }

        contentsArray.put(
            JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
            }
        )

        val requestPayload = JSONObject().apply {
            put("contents", contentsArray)
            put(
                "systemInstruction",
                JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
            )
            put(
                "generationConfig",
                JSONObject().apply {
                    put("temperature", 0.7)
                    put("responseMimeType", "application/json")
                }
            )
        }

        val request = Request.Builder()
            .url(endpoint)
            .post(requestPayload.toString().toRequestBody(jsonMediaType))
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string().orEmpty()

        return if (response.isSuccessful && responseBody.isNotBlank()) {
            val parsedMessage = parseAnyGeminiResponse(responseBody)
            Result.success(parsedMessage)
        } else {
            val errorMsg = try {
                val errJson = JSONObject(responseBody)
                errJson.optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}: $responseBody"
            } catch (e: Exception) {
                "HTTP ${response.code}: $responseBody"
            }
            Result.failure(Exception(errorMsg))
        }
    }

    private fun parseAnyGeminiResponse(rawJson: String): AiChatMessage {
        val rawText = extractTextFromAnyResponse(rawJson)

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

    private fun extractTextFromAnyResponse(rawJson: String): String {
        return try {
            val root = JSONObject(rawJson)

            // 1. Try Interactions API 'outputs'
            val outputs = root.optJSONArray("outputs")
            if (outputs != null && outputs.length() > 0) {
                val lastOutput = outputs.optJSONObject(outputs.length() - 1)
                val text = lastOutput?.optString("text")
                if (!text.isNullOrBlank()) return text
            }

            // 2. Try Interactions API 'steps'
            val steps = root.optJSONArray("steps")
            if (steps != null && steps.length() > 0) {
                for (i in (steps.length() - 1) downTo 0) {
                    val step = steps.optJSONObject(i) ?: continue
                    val output = step.optJSONObject("output")
                    val text = output?.optString("text")
                    if (!text.isNullOrBlank()) return text
                }
            }

            // 3. Try legacy 'candidates' -> 'content' -> 'parts'
            val candidates = root.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val candText = parts?.optJSONObject(0)?.optString("text")
            if (!candText.isNullOrBlank()) return candText

            // 4. Try root 'text' or 'output'
            val rootText = root.optString("text")
            if (rootText.isNotBlank()) return rootText

            rawJson
        } catch (e: Exception) {
            rawJson
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
