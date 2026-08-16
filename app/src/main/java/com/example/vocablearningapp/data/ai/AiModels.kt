package com.example.vocablearningapp.data.ai

import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import java.util.UUID

enum class MessageSender {
    USER,
    AI,
    SYSTEM
}

data class AiGeneratedWord(
    val word: String,
    val ipa: String,
    val partOfSpeech: PartOfSpeech = PartOfSpeech.NOUN,
    val meaning: String,
    val exampleSentence: String
) {
    fun toVocabularyItem(setId: String, index: Int): VocabularyItem {
        return VocabularyItem(
            id = "${setId.lowercase().replace(" ", "-")}-$index-${UUID.randomUUID().toString().take(4)}",
            word = word.trim(),
            meaning = meaning.trim(),
            pronunciation = ipa.trim(),
            partOfSpeech = partOfSpeech,
            exampleSentence = exampleSentence.trim(),
            fsrsState = FsrsState.NEW
        )
    }
}

data class AiGeneratedSet(
    val title: String,
    val description: String,
    val level: String = "B1",
    val category: String = "General",
    val words: List<AiGeneratedWord>
) {
    fun toVocabularySet(): VocabularySet {
        val setId = "ai-${UUID.randomUUID().toString().take(8)}"
        val vocabWords = words.mapIndexed { index, w -> w.toVocabularyItem(setId, index) }
        return VocabularySet(
            id = setId,
            title = title.trim(),
            description = description.trim(),
            category = category.trim(),
            level = level.trim(),
            words = vocabWords
        )
    }
}

sealed class AiActionPayload {
    data class CreateSetPayload(
        val generatedSet: AiGeneratedSet,
        var isSaved: Boolean = false
    ) : AiActionPayload()
}

data class AiChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val actionPayload: AiActionPayload? = null
)
