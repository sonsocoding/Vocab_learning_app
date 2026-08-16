package com.example.vocablearningapp.domain.srs

import com.example.vocablearningapp.domain.model.FsrsRating

enum class QuizModeType {
    FLASHCARD,          // User self-assessment
    MULTIPLE_CHOICE,    // 4-option selection (Learn Mode)
    TRUE_FALSE_QUIZ,    // True / False reflex test (Quiz Mode)
    MATCHING_PAIRS,     // Speed pair matching (Match Mode)
    FILL_IN_BLANK,      // Cloze / Spelling in context (Fill-in-the-Blank Mode)
    SENTENCE_SCRAMBLE   // Word order & syntax puzzle (Sentence Scramble Mode)
}

/**
 * Smart Multi-Factor FSRS Evaluator.
 * 
 * Instead of binary INCORRECT -> AGAIN and CORRECT -> GOOD, this engine evaluates:
 * 1. Accuracy & Attempt count (Failure / Multiple attempts -> AGAIN)
 * 2. Hint Usage (Hint used -> HARD)
 * 3. Response Time Latency (Hesitant -> HARD, Rapid -> EASY)
 * 4. Mode Difficulty Weight (Active recall in Fill-in-Blank / Scramble has higher cognitive load)
 */
object FsrsRatingEvaluator {
    fun evaluate(
        isCorrect: Boolean,
        responseTimeMs: Long,
        modeType: QuizModeType,
        attemptCount: Int = 1,
        hintUsed: Boolean = false
    ): FsrsRating {
        if (!isCorrect || attemptCount > 1) {
            return FsrsRating.AGAIN
        }

        val seconds = responseTimeMs / 1000.0

        if (hintUsed) {
            return FsrsRating.HARD
        }

        return when (modeType) {
            QuizModeType.FILL_IN_BLANK -> {
                when {
                    seconds < 5.0 -> FsrsRating.EASY
                    seconds <= 12.0 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.SENTENCE_SCRAMBLE -> {
                when {
                    seconds < 8.0 -> FsrsRating.EASY
                    seconds <= 18.0 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.MULTIPLE_CHOICE -> {
                when {
                    seconds < 2.5 -> FsrsRating.EASY
                    seconds <= 6.0 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.TRUE_FALSE_QUIZ -> {
                when {
                    seconds < 2.0 -> FsrsRating.EASY
                    seconds <= 5.0 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.MATCHING_PAIRS -> {
                when {
                    seconds < 3.0 -> FsrsRating.EASY
                    seconds <= 7.0 -> FsrsRating.GOOD
                    else -> FsrsRating.HARD
                }
            }
            QuizModeType.FLASHCARD -> FsrsRating.GOOD
        }
    }
}

