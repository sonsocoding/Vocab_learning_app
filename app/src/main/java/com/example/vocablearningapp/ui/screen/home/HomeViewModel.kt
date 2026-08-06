package com.example.vocablearningapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocablearningapp.data.local.entity.LevelEntity
import com.example.vocablearningapp.data.local.entity.UserEntity
import com.example.vocablearningapp.data.repository.UserRepository
import com.example.vocablearningapp.data.repository.VocabRepository
import com.example.vocablearningapp.domain.model.DeckWithProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val user: UserEntity? = null,
    val totalLearnedCount: Int = 0,
    val totalRatedCount: Int = 0,
    val lastDeckProgress: DeckWithProgress? = null,
    val levels: List<LevelEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isLoggedOut: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val userRepository: UserRepository,
    private val vocabRepository: VocabRepository
) : ViewModel() {

    private val _isLoggedOut = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = userRepository.currentUserId.flatMapLatest { userId ->
        if (userId == null) {
            flowOf(HomeUiState(isLoading = false, isLoggedOut = true))
        } else {
            combine(
                userRepository.getUser(userId),
                vocabRepository.getTotalLearnedCount(userId),
                vocabRepository.getTotalRatedCount(userId),
                userRepository.lastStudiedDeckId,
                vocabRepository.getLevels(),
                _isLoggedOut
            ) { args: Array<Any?> ->
                val user = args[0] as UserEntity?
                val learnedCount = args[1] as Int
                val ratedCount = args[2] as Int
                val lastDeckId = args[3] as Long?
                @Suppress("UNCHECKED_CAST")
                val levels = args[4] as List<LevelEntity>
                val loggedOut = args[5] as Boolean

                val lastDeckProgress = if (lastDeckId != null) {
                    vocabRepository.getDeckWithProgress(userId, lastDeckId)
                } else null

                HomeUiState(
                    user = user,
                    totalLearnedCount = learnedCount,
                    totalRatedCount = ratedCount,
                    lastDeckProgress = lastDeckProgress,
                    levels = levels,
                    isLoading = false,
                    isLoggedOut = loggedOut
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _isLoggedOut.update { true }
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val vocabRepository: VocabRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(userRepository, vocabRepository) as T
        }
    }
}
