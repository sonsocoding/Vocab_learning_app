package com.example.vocablearningapp.data

import android.content.Context
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import org.json.JSONArray

data class DictionaryMeaning(
    val partOfSpeech: PartOfSpeech,
    val meaning: String,
    val exampleSentence: String
)

data class DictionaryWord(
    val word: String,
    val ipa: String,
    val level: String,
    val category: String,
    val meanings: List<DictionaryMeaning>
) {
    val formattedMeaning: String
        get() {
            if (meanings.isEmpty()) return ""
            if (meanings.size == 1) return meanings[0].meaning
            return meanings.joinToString("; ") { "${it.partOfSpeech.label}. ${it.meaning}" }
        }

    val formattedExample: String
        get() {
            if (meanings.isEmpty()) return ""
            if (meanings.size == 1) return meanings[0].exampleSentence
            return meanings.joinToString(" | ") { "${it.partOfSpeech.label}. ${it.exampleSentence}" }
        }

    val primaryPartOfSpeech: PartOfSpeech
        get() = meanings.firstOrNull()?.partOfSpeech ?: PartOfSpeech.NOUN

    fun toVocabularyItem(
        setId: String,
        fsrsState: FsrsState = FsrsState.NEW,
        reviewCount: Int = 0,
        stability: Double = 0.0,
        dueAtMillis: Long = 0L
    ): VocabularyItem {
        return VocabularyItem(
            id = "$setId-$word",
            word = word,
            meaning = formattedMeaning,
            pronunciation = ipa,
            partOfSpeech = primaryPartOfSpeech,
            exampleSentence = formattedExample,
            fsrsState = fsrsState,
            reviewCount = reviewCount,
            stability = stability,
            dueAtMillis = dueAtMillis
        )
    }
}

object DictionaryRepository {
    private var wordsList: List<DictionaryWord> = emptyList()
    private var wordsMap: Map<String, DictionaryWord> = emptyMap()
    private val now = System.currentTimeMillis()
    private val oneDay = 86_400_000L

    fun init(context: Context) {
        if (wordsList.isNotEmpty()) return
        try {
            val jsonString = context.assets.open("cefr_dictionary.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val list = ArrayList<DictionaryWord>(jsonArray.length())
            val map = HashMap<String, DictionaryWord>(jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val word = obj.getString("word")
                val ipa = obj.getString("ipa")
                val level = obj.optString("level", "A1")
                val category = obj.optString("category", "General")

                val meaningsArray = obj.getJSONArray("meanings")
                val meanings = ArrayList<DictionaryMeaning>(meaningsArray.length())
                for (j in 0 until meaningsArray.length()) {
                    val mObj = meaningsArray.getJSONObject(j)
                    val posStr = mObj.optString("partOfSpeech", "noun")
                    val pos = when (posStr.lowercase()) {
                        "verb", "v" -> PartOfSpeech.VERB
                        "adj", "adjective" -> PartOfSpeech.ADJECTIVE
                        else -> PartOfSpeech.NOUN
                    }
                    meanings.add(
                        DictionaryMeaning(
                            partOfSpeech = pos,
                            meaning = mObj.optString("meaning", ""),
                            exampleSentence = mObj.optString("exampleSentence", "")
                        )
                    )
                }

                val dictWord = DictionaryWord(word, ipa, level, category, meanings)
                list.add(dictWord)
                map[word.lowercase()] = dictWord
            }
            wordsList = list
            wordsMap = map
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun lookupWord(word: String): DictionaryWord? {
        val clean = word.trim().lowercase()
        return wordsMap[clean]
    }

    fun getDailyWordsForLevel(level: String): List<VocabularyItem> {
        val seeded = MockData.dailyWordsByLevel[level.uppercase()]
        if (!seeded.isNullOrEmpty()) {
            return seeded
        }

        val cleanMatching = wordsList.filter { dictWord ->
            dictWord.level.equals(level, ignoreCase = true) &&
            dictWord.word.all { it.isLetter() } &&
            dictWord.word.length in 3..12 &&
            dictWord.meanings.isNotEmpty()
        }

        val selected = if (cleanMatching.size >= 10) cleanMatching.shuffled().take(10) else wordsList.filter { it.word.all { c -> c.isLetter() } }.shuffled().take(10)

        return selected.map { dictWord ->
            dictWord.toVocabularyItem(setId = "daily-${level.lowercase()}")
        }
    }

    fun searchWords(query: String, limit: Int = 6): List<DictionaryWord> {
        val clean = query.trim().lowercase()
        if (clean.isBlank()) return emptyList()

        val prefixMatches = wordsList.filter { it.word.lowercase().startsWith(clean) }
        val containsMatches = if (prefixMatches.size < limit) {
            wordsList.filter { it.word.lowercase().contains(clean) && !it.word.lowercase().startsWith(clean) }
        } else {
            emptyList()
        }

        return (prefixMatches + containsMatches).take(limit)
    }

    fun getSeedVocabularySets(): List<VocabularySet> {
        return MockData.vocabularySets
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
