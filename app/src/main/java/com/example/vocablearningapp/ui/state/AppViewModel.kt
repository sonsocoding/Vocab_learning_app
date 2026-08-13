package com.example.vocablearningapp.ui.state

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.vocablearningapp.data.MockData
import com.example.vocablearningapp.data.StreakManager
import com.example.vocablearningapp.data.StreakState
import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.domain.srs.FsrsScheduler

data class AppUiState(
    val vocabularySets: List<VocabularySet> = MockData.vocabularySets,
    val dailyWordsByLevel: Map<String, List<VocabularyItem>> = MockData.dailyWordsByLevel,
    val lastStudiedLevel: String = "A2",
    val streakState: StreakState = StreakState()
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val DAILY_WORDS_SET_ID = "daily-words"
        const val DAILY_REVIEW_SET_ID = "daily-review"
        const val DAILY_WORDS_PER_DAY = 10
    }

    private val streakManager = StreakManager(application)

    var uiState by mutableStateOf(AppUiState(streakState = streakManager.getStreakState()))
        private set

    private val fsrsScheduler = FsrsScheduler()

    val sets: List<VocabularySet>
        get() = uiState.vocabularySets

    val allItems: List<VocabularyItem>
        get() = sets.flatMap { it.words }

    val reviewItems: List<VocabularyItem>
        get() = allItems + dailyWordsSet.words

    val dailyWordsSet: VocabularySet
        get() {
            return VocabularySet(
                id = DAILY_WORDS_SET_ID,
                title = "Daily Words",
                description = "Fresh words generated from your recent learning level.",
                category = "Daily practice",
                level = uiState.lastStudiedLevel,
                words = uiState.dailyWordsByLevel[uiState.lastStudiedLevel]
                    .orEmpty()
                    .take(DAILY_WORDS_PER_DAY)
            )
        }

    val todayReviewSet: VocabularySet
        get() {
            val nowMillis = System.currentTimeMillis()
            return VocabularySet(
                id = DAILY_REVIEW_SET_ID,
                title = "Daily Review",
                description = "Words scheduled by your spaced repetition review plan.",
                category = "Review",
                level = uiState.lastStudiedLevel,
                words = reviewItems.filter { fsrsScheduler.isDue(it, nowMillis) }
            )
        }

    fun setById(id: String): VocabularySet? = when (id) {
        DAILY_WORDS_SET_ID -> dailyWordsSet
        DAILY_REVIEW_SET_ID -> todayReviewSet
        else -> sets.firstOrNull { it.id == id }
    }

    fun rateItem(
        itemId: String,
        rating: FsrsRating,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        val currentItem = reviewItems.firstOrNull { it.id == itemId } ?: return
        val schedule = fsrsScheduler.review(currentItem, rating, nowMillis)
        val updatedItem = currentItem.copy(
            fsrsState = schedule.state,
            fsrsStep = schedule.step,
            stability = schedule.stability,
            difficulty = schedule.difficulty,
            dueAtMillis = schedule.dueAtMillis,
            lastReviewAtMillis = nowMillis,
            scheduledDays = schedule.scheduledDays,
            reviewCount = currentItem.reviewCount + 1,
            lapseCount = currentItem.lapseCount +
                if (currentItem.fsrsState == FsrsState.REVIEW && rating == FsrsRating.AGAIN) 1 else 0
        )
        val studiedSetLevel = sets.firstOrNull { set ->
            set.words.any { item -> item.id == itemId }
        }?.level
        uiState = uiState.copy(
            vocabularySets = sets.map { set ->
                set.copy(
                    words = set.words.map { item ->
                        if (item.id == itemId) {
                            updatedItem
                        } else item
                    }
                )
            },
            dailyWordsByLevel = uiState.dailyWordsByLevel.mapValues { (_, items) ->
                items.map { item ->
                    if (item.id == itemId) {
                        updatedItem
                    } else item
                }
            },
            lastStudiedLevel = studiedSetLevel ?: uiState.lastStudiedLevel,
            streakState = streakManager.recordActivityToday()
        )
    }

    fun recordActivity() {
        uiState = uiState.copy(streakState = streakManager.recordActivityToday())
    }

    fun skipDailyWord(itemId: String) {
        uiState = uiState.copy(
            dailyWordsByLevel = uiState.dailyWordsByLevel.mapValues { (_, items) ->
                items.filterNot { it.id == itemId }
            }
        )
    }

    fun learnDailyWord(itemId: String) {
        rateItem(itemId, FsrsRating.GOOD)
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
