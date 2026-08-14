package com.example.vocablearningapp.domain.srs

import com.example.vocablearningapp.domain.model.FsrsRating

enum class QuizModeType {
    FLASHCARD,       // User self-assessment
    MULTIPLE_CHOICE, // 4-option selection
    TYPING_TEST,     // Full spelling recall
    MATCHING_PAIRS   // Speed pair matching
}

/**
 * Smart Multi-Factor FSRS Evaluator.
 * 
 * Instead of binary INCORRECT -> AGAIN and CORRECT -> GOOD, this engine evaluates:
 * 1. Accuracy & Attempt count (Failure / Multiple hints -> AGAIN)
 * 2. Response Time Latency (Hesitant > 5.5s -> HARD, Instant < 2.0s -> EASY)
 * 3. Mode Difficulty Weight (Typing test active recall -> EASY/GOOD boost)
 */
object FsrsRatingEvaluator {
    fun evaluate(
        isCorrect: Boolean,
        responseTimeMs: Long,
        modeType: QuizModeType,
        attemptCount: Int = 1
    ): FsrsRating {
        if (!isCorrect || attemptCount > 1) {
            return FsrsRating.AGAIN
        }

        val seconds = responseTimeMs / 1000.0

        return when (modeType) {
            QuizModeType.TYPING_TEST -> {
                if (seconds < 4.0) FsrsRating.EASY else FsrsRating.GOOD
            }
            QuizModeType.MULTIPLE_CHOICE -> {
                when {
                    seconds < 2.0 -> FsrsRating.EASY
                    seconds <= 5.5 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.MATCHING_PAIRS -> {
                when {
                    seconds < 2.5 -> FsrsRating.EASY
                    seconds <= 6.0 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.FLASHCARD -> FsrsRating.GOOD
        }
    }
}
