package com.example.vocablearningapp.domain.srs

import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem

data class ReviewSchedule(
    val nextReviewAtMillis: Long,
    val intervalDays: Int
)

object SpacedRepetition {
    private const val MILLIS_PER_MINUTE = 60_000L
    private const val MILLIS_PER_DAY = 24 * 60 * MILLIS_PER_MINUTE
    private const val FORGOT_DELAY_MINUTES = 10L
    private const val FIRST_MASTERED_INTERVAL_DAYS = 3
    private const val MAX_INTERVAL_DAYS = 60

    fun schedule(
        item: VocabularyItem,
        rating: MemoryLevel,
        nowMillis: Long
    ): ReviewSchedule {
        return when (rating) {
            MemoryLevel.FORGOT -> ReviewSchedule(
                nextReviewAtMillis = nowMillis + FORGOT_DELAY_MINUTES * MILLIS_PER_MINUTE,
                intervalDays = 0
            )

            MemoryLevel.LEARNING -> ReviewSchedule(
                nextReviewAtMillis = nowMillis + MILLIS_PER_DAY,
                intervalDays = 1
            )

            MemoryLevel.MASTERED -> {
                val nextInterval = if (item.reviewIntervalDays == 0) {
                    FIRST_MASTERED_INTERVAL_DAYS
                } else {
                    (item.reviewIntervalDays * 2).coerceAtMost(MAX_INTERVAL_DAYS)
                }
                ReviewSchedule(
                    nextReviewAtMillis = nowMillis + nextInterval * MILLIS_PER_DAY,
                    intervalDays = nextInterval
                )
            }
        }
    }

    fun isDue(item: VocabularyItem, nowMillis: Long): Boolean {
        return item.nextReviewAtMillis <= nowMillis
    }
}
