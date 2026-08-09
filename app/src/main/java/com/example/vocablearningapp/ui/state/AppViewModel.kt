package com.example.vocablearningapp.ui.state

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.vocablearningapp.data.MockData
import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.domain.srs.SpacedRepetition

data class AppUiState(
    val vocabularySets: List<VocabularySet> = MockData.vocabularySets,
    val dailyWordsByLevel: Map<String, List<VocabularyItem>> = MockData.dailyWordsByLevel,
    val lastStudiedLevel: String = "A2"
)

class AppViewModel : ViewModel() {
    companion object {
        const val DAILY_WORDS_SET_ID = "daily-words"
        const val TODAY_REVIEW_SET_ID = "today-review"
    }

    var uiState by mutableStateOf(AppUiState())
        private set

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
                words = uiState.dailyWordsByLevel[uiState.lastStudiedLevel].orEmpty()
            )
        }

    val todayReviewSet: VocabularySet
        get() {
            val nowMillis = System.currentTimeMillis()
            return VocabularySet(
                id = TODAY_REVIEW_SET_ID,
                title = "Today's Review",
                description = "Words scheduled by your spaced repetition review plan.",
                category = "Review",
                level = uiState.lastStudiedLevel,
                words = reviewItems.filter { SpacedRepetition.isDue(it, nowMillis) }
            )
        }

    fun setById(id: String): VocabularySet? = when (id) {
        DAILY_WORDS_SET_ID -> dailyWordsSet
        TODAY_REVIEW_SET_ID -> todayReviewSet
        else -> sets.firstOrNull { it.id == id }
    }

    fun rateItem(
        itemId: String,
        memoryLevel: MemoryLevel,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        val currentItem = reviewItems.firstOrNull { it.id == itemId } ?: return
        val schedule = SpacedRepetition.schedule(currentItem, memoryLevel, nowMillis)
        val studiedSetLevel = sets.firstOrNull { set ->
            set.words.any { item -> item.id == itemId }
        }?.level
        uiState = uiState.copy(
            vocabularySets = sets.map { set ->
                set.copy(
                    words = set.words.map { item ->
                        if (item.id == itemId) {
                            item.copy(
                                memoryLevel = memoryLevel,
                                nextReviewAtMillis = schedule.nextReviewAtMillis,
                                reviewIntervalDays = schedule.intervalDays
                            )
                        } else item
                    }
                )
            },
            dailyWordsByLevel = uiState.dailyWordsByLevel.mapValues { (_, items) ->
                items.map { item ->
                    if (item.id == itemId) {
                        item.copy(
                            memoryLevel = memoryLevel,
                            nextReviewAtMillis = schedule.nextReviewAtMillis,
                            reviewIntervalDays = schedule.intervalDays
                        )
                    } else item
                }
            },
            lastStudiedLevel = studiedSetLevel ?: uiState.lastStudiedLevel
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
