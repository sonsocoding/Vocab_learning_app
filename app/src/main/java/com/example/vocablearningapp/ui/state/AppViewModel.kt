package com.example.vocablearningapp.ui.state

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.vocablearningapp.data.MockData
import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet

data class AppUiState(
    val vocabularySets: List<VocabularySet> = MockData.vocabularySets
)

class AppViewModel : ViewModel() {
    var uiState by mutableStateOf(AppUiState())
        private set

    val sets: List<VocabularySet>
        get() = uiState.vocabularySets

    val allItems: List<VocabularyItem>
        get() = sets.flatMap { it.words }

    fun setById(id: String): VocabularySet? = sets.firstOrNull { it.id == id }

    fun rateItem(itemId: String, memoryLevel: MemoryLevel) {
        uiState = uiState.copy(
            vocabularySets = sets.map { set ->
                set.copy(
                    words = set.words.map { item ->
                        if (item.id == itemId) item.copy(memoryLevel = memoryLevel) else item
                    }
                )
            }
        )
    }

    fun saveSet(
        setId: String?,
        title: String,
        description: String,
        level: String,
        category: String,
        words: List<VocabularyItem>
    ) {
        val safeTitle = title.trim().ifBlank { "Untitled set" }
        val id = setId?.takeUnless { it == "new" } ?: "set-${System.currentTimeMillis()}"
        val updatedSet = VocabularySet(
            id = id,
            title = safeTitle,
            description = description.trim(),
            category = category.trim().ifBlank { "General" },
            level = level.trim().ifBlank { "A2" },
            words = words
        )

        val existing = sets.any { it.id == id }
        uiState = uiState.copy(
            vocabularySets = if (existing) {
                sets.map { if (it.id == id) updatedSet else it }
            } else {
                sets + updatedSet
            }
        )
    }
}
