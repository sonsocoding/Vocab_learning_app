package com.example.vocablearningapp.ui.screen.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocablearningapp.data.local.entity.TopicEntity
import com.example.vocablearningapp.data.repository.UserRepository
import com.example.vocablearningapp.data.repository.VocabRepository
import com.example.vocablearningapp.domain.model.DeckWithProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeckUiState(
    val topic: TopicEntity? = null,
    val decks: List<DeckWithProgress> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class DeckViewModel(
    private val userRepository: UserRepository,
    private val vocabRepository: VocabRepository
) : ViewModel() {

    private val _topicId = MutableStateFlow<Long?>(null)
    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()

    fun loadDecks(topicId: Long) {
        _topicId.value = topicId
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val topic = vocabRepository.getTopicById(topicId)
            _uiState.update { it.copy(topic = topic) }

            userRepository.currentUserId.flatMapLatest { userId ->
                if (userId != null) {
                    vocabRepository.getDecksByTopic(userId, topicId)
                } else flowOf(emptyList())
            }.collect { decksWithProgress ->
                _uiState.update { it.copy(decks = decksWithProgress, isLoading = false) }
            }
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val vocabRepository: VocabRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeckViewModel(userRepository, vocabRepository) as T
        }
    }
}
