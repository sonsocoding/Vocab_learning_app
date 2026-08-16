package com.example.vocablearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary_sets")
data class VocabularySetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val level: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)
