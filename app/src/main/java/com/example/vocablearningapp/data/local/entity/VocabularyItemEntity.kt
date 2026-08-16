package com.example.vocablearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vocabulary_items",
    foreignKeys = [
        ForeignKey(
            entity = VocabularySetEntity::class,
            parentColumns = ["id"],
            childColumns = ["setId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["setId"])]
)
data class VocabularyItemEntity(
    @PrimaryKey val id: String,
    val setId: String,
    val word: String,
    val meaning: String,
    val pronunciation: String,
    val partOfSpeech: String,
    val exampleSentence: String,
    val fsrsState: String,
    val fsrsStep: Int? = null,
    val stability: Double = 0.0,
    val difficulty: Double = 0.0,
    val dueAtMillis: Long = 0L,
    val lastReviewAtMillis: Long? = null,
    val scheduledDays: Int = 0,
    val reviewCount: Int = 0,
    val lapseCount: Int = 0,
    val orderIndex: Int = 0
)
