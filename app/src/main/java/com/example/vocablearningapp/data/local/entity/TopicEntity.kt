package com.example.vocablearningapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = LevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["levelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["levelId"])]
)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val levelId: String,
    val title: String,
    val description: String
)
