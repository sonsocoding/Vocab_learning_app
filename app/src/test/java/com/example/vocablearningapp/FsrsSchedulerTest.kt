package com.example.vocablearningapp

import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.srs.FsrsScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FsrsSchedulerTest {
    private val nowMillis = 1_000_000L
    private val dayMillis = 24 * 60 * 60 * 1_000L
    private val scheduler = FsrsScheduler(enableFuzzing = false)

    @Test
    fun newCardAgainStartsTheFirstLearningStep() {
        val schedule = scheduler.review(item(), FsrsRating.AGAIN, nowMillis)

        assertEquals(FsrsState.LEARNING, schedule.state)
        assertEquals(0, schedule.step)
        assertEquals(nowMillis + 60 * 1_000L, schedule.dueAtMillis)
        assertEquals(0, schedule.scheduledDays)
    }

    @Test
    fun newCardGoodAdvancesToTheSecondLearningStep() {
        val schedule = scheduler.review(item(), FsrsRating.GOOD, nowMillis)

        assertEquals(FsrsState.LEARNING, schedule.state)
        assertEquals(1, schedule.step)
        assertEquals(nowMillis + 10 * 60 * 1_000L, schedule.dueAtMillis)
    }

    @Test
    fun hardStaysOnTheCurrentLearningStepAndEasyGraduatesImmediately() {
        val learningCard = item().copy(
            fsrsState = FsrsState.LEARNING,
            fsrsStep = 0,
            stability = 1.0,
            difficulty = 5.0,
            lastReviewAtMillis = nowMillis
        )

        val hard = scheduler.review(learningCard, FsrsRating.HARD, nowMillis)
        val easy = scheduler.review(learningCard, FsrsRating.EASY, nowMillis)

        assertEquals(FsrsState.LEARNING, hard.state)
        assertEquals(0, hard.step)
        assertEquals(nowMillis + 5 * 60 * 1_000L + 30 * 1_000L, hard.dueAtMillis)
        assertEquals(FsrsState.REVIEW, easy.state)
        assertTrue(easy.scheduledDays >= 1)
    }

    @Test
    fun finalLearningStepGraduatesToReview() {
        val learningCard = item().copy(
            fsrsState = FsrsState.LEARNING,
            fsrsStep = 1,
            stability = 1.0,
            difficulty = 5.0,
            lastReviewAtMillis = nowMillis - dayMillis
        )

        val schedule = scheduler.review(learningCard, FsrsRating.GOOD, nowMillis)

        assertEquals(FsrsState.REVIEW, schedule.state)
        assertEquals(null, schedule.step)
        assertTrue(schedule.stability > 0.0)
        assertTrue(schedule.scheduledDays >= 1)
        assertEquals(nowMillis + schedule.scheduledDays * dayMillis, schedule.dueAtMillis)
    }

    @Test
    fun reviewAgainMovesCardIntoRelearning() {
        val reviewCard = item().copy(
            fsrsState = FsrsState.REVIEW,
            stability = 4.0,
            difficulty = 5.0,
            dueAtMillis = nowMillis,
            lastReviewAtMillis = nowMillis - dayMillis
        )

        val schedule = scheduler.review(reviewCard, FsrsRating.AGAIN, nowMillis)

        assertEquals(FsrsState.RELEARNING, schedule.state)
        assertEquals(0, schedule.step)
        assertEquals(nowMillis + 10 * 60 * 1_000L, schedule.dueAtMillis)
        assertTrue(schedule.stability > 0.0)
    }

    @Test
    fun sameDayGoodUsesShortTermStability() {
        val reviewCard = item().copy(
            fsrsState = FsrsState.REVIEW,
            stability = 2.0,
            difficulty = 5.0,
            dueAtMillis = nowMillis,
            lastReviewAtMillis = nowMillis - 5 * 60 * 1_000L
        )

        val schedule = scheduler.review(reviewCard, FsrsRating.GOOD, nowMillis)

        assertTrue(schedule.stability >= reviewCard.stability)
        assertEquals(FsrsState.REVIEW, schedule.state)
    }

    @Test
    fun retrievabilityIsNinetyPercentAtOneStabilityInterval() {
        val card = item().copy(
            stability = 2.0,
            lastReviewAtMillis = nowMillis - 2 * dayMillis
        )

        assertEquals(0.9, scheduler.retrievability(card, nowMillis), 0.01)
    }

    @Test
    fun dueDateControlsDailyReviewEligibility() {
        assertTrue(scheduler.isDue(item().copy(dueAtMillis = 0L), nowMillis))
        assertFalse(scheduler.isDue(item().copy(dueAtMillis = nowMillis + 1L), nowMillis))
    }

    private fun item() = VocabularyItem(
        id = "test-word",
        word = "word",
        meaning = "từ",
        pronunciation = "/wɜːd/",
        exampleSentence = "This is a word."
    )
}
