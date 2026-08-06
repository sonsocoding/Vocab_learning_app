package com.example.vocablearningapp.ui.screen.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocablearningapp.data.local.entity.DeckEntity
import com.example.vocablearningapp.data.repository.VocabRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudyResultUiState(
    val deck: DeckEntity? = null,
    val totalWords: Int = 0,
    val hardCount: Int = 0,
    val somewhatCount: Int = 0,
    val rememberedCount: Int = 0,
    val veryWellCount: Int = 0,
    val isLoading: Boolean = true
) {
    val rememberedRatio: Int
        get() {
            val count = rememberedCount + veryWellCount
            return if (totalWords > 0) ((count.toFloat() / totalWords) * 100).toInt() else 0
        }
}

class StudyResultViewModel(
    private val vocabRepository: VocabRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyResultUiState())
    val uiState: StateFlow<StudyResultUiState> = _uiState.asStateFlow()

    fun loadResult(
        deckId: Long,
        totalWords: Int,
        hardCount: Int,
        somewhatCount: Int,
        rememberedCount: Int,
        veryWellCount: Int
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val deck = vocabRepository.getDeckById(deckId)
            _uiState.update {
                it.copy(
                    deck = deck,
                    totalWords = totalWords,
                    hardCount = hardCount,
                    somewhatCount = somewhatCount,
                    rememberedCount = rememberedCount,
                    veryWellCount = veryWellCount,
                    isLoading = false
                )
            }
        }
    }

    class Factory(private val vocabRepository: VocabRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StudyResultViewModel(vocabRepository) as T
        }
    }
}
