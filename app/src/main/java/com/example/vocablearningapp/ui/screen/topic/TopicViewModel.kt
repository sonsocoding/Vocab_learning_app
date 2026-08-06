package com.example.vocablearningapp.ui.screen.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocablearningapp.data.local.entity.TopicEntity
import com.example.vocablearningapp.data.repository.VocabRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TopicUiState(
    val levelId: String = "",
    val topics: List<TopicEntity> = emptyList(),
    val isLoading: Boolean = true
)

class TopicViewModel(
    private val vocabRepository: VocabRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TopicUiState())
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    fun loadTopics(levelId: String) {
        _uiState.update { it.copy(levelId = levelId, isLoading = true) }
        viewModelScope.launch {
            vocabRepository.getTopicsByLevel(levelId).collect { topics ->
                _uiState.update { it.copy(topics = topics, isLoading = false) }
            }
        }
    }

    class Factory(private val vocabRepository: VocabRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TopicViewModel(vocabRepository) as T
        }
    }
}
