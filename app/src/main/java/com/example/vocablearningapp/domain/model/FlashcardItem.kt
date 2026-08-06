package com.example.vocablearningapp.domain.model

import com.example.vocablearningapp.data.local.entity.FlashcardProgressEntity
import com.example.vocablearningapp.data.local.entity.VocabularyEntity

data class FlashcardItem(
    val vocabulary: VocabularyEntity,
    val progress: FlashcardProgressEntity?
)
