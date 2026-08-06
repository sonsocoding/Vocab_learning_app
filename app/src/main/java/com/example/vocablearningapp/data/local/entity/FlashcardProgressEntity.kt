package com.example.vocablearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcard_progress",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VocabularyEntity::class,
            parentColumns = ["id"],
            childColumns = ["vocabularyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId", "vocabularyId"], unique = true),
        Index(value = ["vocabularyId"])
    ]
)
data class FlashcardProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val vocabularyId: Long,
    val memoryLevel: Int, // 0: Khó nhớ, 1: Hơi nhớ, 2: Nhớ, 3: Nhớ rất rõ
    val reviewCount: Int = 1,
    val lastReviewedAt: Long = System.currentTimeMillis()
)
