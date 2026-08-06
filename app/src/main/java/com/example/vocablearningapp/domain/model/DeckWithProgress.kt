package com.example.vocablearningapp.domain.model

import com.example.vocablearningapp.data.local.entity.DeckEntity

data class DeckWithProgress(
    val deck: DeckEntity,
    val totalWords: Int,
    val learnedWords: Int
)
