package com.example.vocablearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class LevelEntity(
    @PrimaryKey
    val id: String, // e.g., "A1", "A2", "B1"
    val code: String,
    val name: String,
    val description: String,
    val orderNumber: Int
)
