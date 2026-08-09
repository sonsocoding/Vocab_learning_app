package com.example.vocablearningapp

import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.srs.SpacedRepetition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpacedRepetitionTest {
    private val nowMillis = 1_000_000L

    @Test
    fun forgotSchedulesAQuickRetry() {
        val schedule = SpacedRepetition.schedule(item(), MemoryLevel.FORGOT, nowMillis)

        assertEquals(nowMillis + 10 * 60 * 1_000L, schedule.nextReviewAtMillis)
        assertEquals(0, schedule.intervalDays)
    }

    @Test
    fun learningSchedulesTheNextDay() {
        val schedule = SpacedRepetition.schedule(item(), MemoryLevel.LEARNING, nowMillis)

        assertEquals(nowMillis + 24 * 60 * 60 * 1_000L, schedule.nextReviewAtMillis)
        assertEquals(1, schedule.intervalDays)
    }

    @Test
    fun masteredGrowsTheInterval() {
        val schedule = SpacedRepetition.schedule(
            item().copy(reviewIntervalDays = 3),
            MemoryLevel.MASTERED,
            nowMillis
        )

        assertEquals(nowMillis + 6 * 24 * 60 * 60 * 1_000L, schedule.nextReviewAtMillis)
        assertEquals(6, schedule.intervalDays)
    }

    @Test
    fun zeroTimestampIsDueAndFutureTimestampIsNotDue() {
        assertTrue(SpacedRepetition.isDue(item().copy(nextReviewAtMillis = 0L), nowMillis))
        assertFalse(SpacedRepetition.isDue(item().copy(nextReviewAtMillis = nowMillis + 1L), nowMillis))
    }

    private fun item() = VocabularyItem(
        id = "test-word",
        word = "word",
        meaning = "từ",
        pronunciation = "/wɜːd/",
        exampleSentence = "This is a word."
    )
}
