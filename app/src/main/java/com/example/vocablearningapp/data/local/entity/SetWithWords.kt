package com.example.vocablearningapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet

data class SetWithWords(
    @Embedded val set: VocabularySetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "setId"
    )
    val words: List<VocabularyItemEntity>
) {
    fun toDomainModel(): VocabularySet {
        return VocabularySet(
            id = set.id,
            title = set.title,
            description = set.description,
            category = set.category,
            level = set.level,
            words = words.sortedBy { it.orderIndex }.map { it.toDomainModel() }
        )
    }
}

fun VocabularyItemEntity.toDomainModel(): VocabularyItem {
    return VocabularyItem(
        id = id,
        word = word,
        meaning = meaning,
        pronunciation = pronunciation,
        partOfSpeech = when (partOfSpeech.lowercase()) {
            "verb", "v" -> PartOfSpeech.VERB
            "adj", "adjective" -> PartOfSpeech.ADJECTIVE
            else -> PartOfSpeech.NOUN
        },
        exampleSentence = exampleSentence,
        fsrsState = try { FsrsState.valueOf(fsrsState) } catch (e: Exception) { FsrsState.NEW },
        fsrsStep = fsrsStep,
        stability = stability,
        difficulty = difficulty,
        dueAtMillis = dueAtMillis,
        lastReviewAtMillis = lastReviewAtMillis,
        scheduledDays = scheduledDays,
        reviewCount = reviewCount,
        lapseCount = lapseCount
    )
}

fun VocabularyItem.toEntity(setId: String, orderIndex: Int = 0): VocabularyItemEntity {
    return VocabularyItemEntity(
        id = id,
        setId = setId,
        word = word,
        meaning = meaning,
        pronunciation = pronunciation,
        partOfSpeech = partOfSpeech.name,
        exampleSentence = exampleSentence,
        fsrsState = fsrsState.name,
        fsrsStep = fsrsStep,
        stability = stability,
        difficulty = difficulty,
        dueAtMillis = dueAtMillis,
        lastReviewAtMillis = lastReviewAtMillis,
        scheduledDays = scheduledDays,
        reviewCount = reviewCount,
        lapseCount = lapseCount,
        orderIndex = orderIndex
    )
}

fun VocabularySet.toEntity(): VocabularySetEntity {
    return VocabularySetEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        level = level
    )
}
