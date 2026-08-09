package com.example.vocablearningapp.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocablearningapp.data.local.entity.DeckEntity
import com.example.vocablearningapp.data.repository.UserRepository
import com.example.vocablearningapp.data.repository.VocabRepository
import com.example.vocablearningapp.domain.model.FlashcardItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FlashcardUiState(
    val deck: DeckEntity? = null,
    val items: List<FlashcardItem> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val isFinished: Boolean = false,
    val hardCount: Int = 0,     // Chưa nhớ (Level 0)
    val somewhatCount: Int = 0, // Hơi nhớ (Level 1)
    val rememberedCount: Int = 0 // Đã nhớ (Level 2)
)

class FlashcardViewModel(
    private val userRepository: UserRepository,
    private val vocabRepository: VocabRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    fun loadDeck(deckId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = userRepository.currentUserId.firstOrNull() ?: return@launch
            val deck = vocabRepository.getDeckById(deckId)
            val items = vocabRepository.getFlashcardItemsForDeck(userId, deckId)

            userRepository.saveLastStudiedDeck(deckId)

            _uiState.update {
                it.copy(
                    deck = deck,
                    items = items,
                    currentIndex = 0,
                    isFlipped = false,
                    isLoading = false,
                    isFinished = false,
                    hardCount = 0,
                    somewhatCount = 0,
                    rememberedCount = 0
                )
            }
        }
    }

    fun flipCard() {
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }

    fun nextCard() {
        val state = _uiState.value
        if (state.currentIndex < state.items.size - 1) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    isFlipped = false
                )
            }
        }
    }

    fun previousCard() {
        val state = _uiState.value
        if (state.currentIndex > 0) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex - 1,
                    isFlipped = false
                )
            }
        }
    }

    fun rateMemoryLevel(level: Int) {
        val state = _uiState.value
        val currentItem = state.items.getOrNull(state.currentIndex) ?: return

        viewModelScope.launch {
            val userId = userRepository.currentUserId.firstOrNull() ?: return@launch
            vocabRepository.updateFlashcardProgress(
                userId = userId,
                vocabularyId = currentItem.vocabulary.id,
                memoryLevel = level
            )

            val newHard = if (level == 0) state.hardCount + 1 else state.hardCount
            val newSomewhat = if (level == 1) state.somewhatCount + 1 else state.somewhatCount
            val newRemembered = if (level >= 2) state.rememberedCount + 1 else state.rememberedCount

            if (state.currentIndex < state.items.size - 1) {
                _uiState.update {
                    it.copy(
                        currentIndex = it.currentIndex + 1,
                        isFlipped = false,
                        hardCount = newHard,
                        somewhatCount = newSomewhat,
                        rememberedCount = newRemembered
                    )
                }
            } else {
                // All cards rated - finish session safely
                _uiState.update {
                    it.copy(
                        isFinished = true,
                        hardCount = newHard,
                        somewhatCount = newSomewhat,
                        rememberedCount = newRemembered
                    )
                }
            }
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val vocabRepository: VocabRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FlashcardViewModel(userRepository, vocabRepository) as T
        }
    }
}
