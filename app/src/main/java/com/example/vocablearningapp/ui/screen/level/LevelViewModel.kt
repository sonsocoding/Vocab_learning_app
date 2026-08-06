package com.example.vocablearningapp.ui.screen.level

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocablearningapp.data.local.entity.LevelEntity
import com.example.vocablearningapp.data.repository.VocabRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LevelUiState(
    val levels: List<LevelEntity> = emptyList(),
    val isLoading: Boolean = true
)

class LevelViewModel(
    vocabRepository: VocabRepository
) : ViewModel() {

    val uiState: StateFlow<LevelUiState> = vocabRepository.getLevels()
        .map { levels -> LevelUiState(levels = levels, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LevelUiState(isLoading = true)
        )

    class Factory(private val vocabRepository: VocabRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LevelViewModel(vocabRepository) as T
        }
    }
}
