package com.example.vocablearningapp.domain.srs

import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.VocabularyItem
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

data class FsrsSchedule(
    val state: FsrsState,
    val step: Int?,
    val stability: Double,
    val difficulty: Double,
    val dueAtMillis: Long,
    val scheduledDays: Int
)

/**
 * A small, dependency-free implementation of the FSRS-6 scheduler.
 *
 * The defaults match the current py-fsrs scheduler: 90% desired retention,
 * 1-minute and 10-minute learning steps, a 10-minute relearning step, and
 * the published 21 FSRS-6 parameters.
 */
class FsrsScheduler(
    private val parameters: List<Double> = DEFAULT_PARAMETERS,
    private val desiredRetention: Double = 0.9,
    private val learningStepsMillis: List<Long> = listOf(MINUTE, 10 * MINUTE),
    private val relearningStepsMillis: List<Long> = listOf(10 * MINUTE),
    private val maximumIntervalDays: Int = 36_500,
    private val enableFuzzing: Boolean = true,
    private val random: Random = Random.Default
) {
    private val decay = -parameters[20]
    private val factor = DEFAULT_RETENTION.pow(1 / decay) - 1

    init {
        require(parameters.size == 21) { "FSRS-6 requires exactly 21 parameters." }
        require(desiredRetention in 0.0..1.0 && desiredRetention > 0.0) {
            "Desired retention must be greater than 0 and at most 1."
        }
        require(maximumIntervalDays > 0) { "Maximum interval must be positive." }
    }

    fun review(
        item: VocabularyItem,
        rating: FsrsRating,
        nowMillis: Long
    ): FsrsSchedule {
        var state = item.fsrsState
        var step = item.fsrsStep ?: if (state == FsrsState.NEW) 0 else 0
        var stability = item.stability
        var difficulty = item.difficulty
        val elapsedDays = item.lastReviewAtMillis?.let { lastReview ->
            max(0L, (nowMillis - lastReview) / MILLIS_PER_DAY)
        }

        if (state == FsrsState.NEW) {
            state = FsrsState.LEARNING
            step = 0
        }

        val hasModel = stability > 0.0 && difficulty > 0.0
        if (!hasModel) {
            stability = initialStability(rating)
            difficulty = initialDifficulty(rating, clamp = true)
        } else if (state == FsrsState.LEARNING || state == FsrsState.RELEARNING) {
            if (elapsedDays != null && elapsedDays < 1) {
                stability = shortTermStability(stability, rating)
                difficulty = nextDifficulty(difficulty, rating)
            } else if (item.lastReviewAtMillis != null) {
                stability = nextStability(item, stability, difficulty, rating, nowMillis)
                difficulty = nextDifficulty(difficulty, rating)
            }
        } else if (state == FsrsState.REVIEW) {
            stability = if (elapsedDays != null && elapsedDays < 1) {
                shortTermStability(stability, rating)
            } else {
                nextStability(item, stability, difficulty, rating, nowMillis)
            }
            difficulty = nextDifficulty(difficulty, rating)
        }

        val transition = when (state) {
            FsrsState.LEARNING -> learningTransition(
                currentState = FsrsState.LEARNING,
                currentStep = step,
                rating = rating,
                steps = learningStepsMillis
            )

            FsrsState.RELEARNING -> learningTransition(
                currentState = FsrsState.RELEARNING,
                currentStep = step,
                rating = rating,
                steps = relearningStepsMillis
            )

            FsrsState.REVIEW -> reviewTransition(rating)
            FsrsState.NEW -> error("New cards are normalized to Learning before scheduling.")
        }

        val rawIntervalDays = if (transition.useLongTermInterval) {
            nextInterval(stability)
        } else {
            0
        }
        val scheduledDays = if (transition.useLongTermInterval) {
            if (enableFuzzing && transition.state == FsrsState.REVIEW) {
                fuzzInterval(rawIntervalDays)
            } else {
                rawIntervalDays
            }
        } else {
            0
        }
        val delayMillis = if (transition.useLongTermInterval) {
            scheduledDays * MILLIS_PER_DAY
        } else {
            transition.delayMillis
        }

        return FsrsSchedule(
            state = transition.state,
            step = transition.step,
            stability = clampStability(stability),
            difficulty = clampDifficulty(difficulty),
            dueAtMillis = nowMillis + delayMillis,
            scheduledDays = scheduledDays
        )
    }

    fun isDue(item: VocabularyItem, nowMillis: Long): Boolean = item.dueAtMillis <= nowMillis

    fun retrievability(item: VocabularyItem, nowMillis: Long): Double {
        val lastReviewAtMillis = item.lastReviewAtMillis ?: return 0.0
        if (item.stability <= 0.0) return 0.0
        val elapsedDays = max(0L, (nowMillis - lastReviewAtMillis) / MILLIS_PER_DAY)
        return (1 + factor * elapsedDays / item.stability).pow(decay)
    }

    private fun learningTransition(
        currentState: FsrsState,
        currentStep: Int,
        rating: FsrsRating,
        steps: List<Long>
    ): Transition {
        if (steps.isEmpty()) return Transition(FsrsState.REVIEW, null, useLongTermInterval = true)

        val safeStep = currentStep.coerceAtLeast(0)
        if (safeStep >= steps.size && rating != FsrsRating.AGAIN) {
            return Transition(FsrsState.REVIEW, null, useLongTermInterval = true)
        }

        return when (rating) {
            FsrsRating.AGAIN -> Transition(currentState, 0, delayMillis = steps.first())
            FsrsRating.HARD -> {
                val hardDelay = when {
                    safeStep == 0 && steps.size == 1 -> (steps[0] * 1.5).roundToInt().toLong()
                    safeStep == 0 && steps.size >= 2 -> (steps[0] + steps[1]) / 2
                    else -> steps[safeStep.coerceAtMost(steps.lastIndex)]
                }
                Transition(currentState, safeStep.coerceAtMost(steps.lastIndex), delayMillis = hardDelay)
            }

            FsrsRating.GOOD -> {
                if (safeStep + 1 == steps.size) {
                    Transition(FsrsState.REVIEW, null, useLongTermInterval = true)
                } else {
                    Transition(currentState, safeStep + 1, delayMillis = steps[safeStep + 1])
                }
            }

            FsrsRating.EASY -> Transition(FsrsState.REVIEW, null, useLongTermInterval = true)
        }
    }

    private fun reviewTransition(rating: FsrsRating): Transition {
        return if (rating == FsrsRating.AGAIN && relearningStepsMillis.isNotEmpty()) {
            Transition(FsrsState.RELEARNING, 0, delayMillis = relearningStepsMillis.first())
        } else {
            Transition(FsrsState.REVIEW, null, useLongTermInterval = true)
        }
    }

    private fun initialStability(rating: FsrsRating): Double {
        return clampStability(parameters[rating.value - 1])
    }

    private fun initialDifficulty(rating: FsrsRating, clamp: Boolean): Double {
        val initialDifficulty = parameters[4] - exp(parameters[5] * (rating.value - 1)) + 1
        return if (clamp) clampDifficulty(initialDifficulty) else initialDifficulty
    }

    private fun nextInterval(stability: Double): Int {
        val interval = ((stability / factor) * (desiredRetention.pow(1 / decay) - 1)).roundToInt()
        return interval.coerceIn(1, maximumIntervalDays)
    }

    private fun shortTermStability(stability: Double, rating: FsrsRating): Double {
        var increase = exp(parameters[17] * (rating.value - 3 + parameters[18])) * stability.pow(-parameters[19])
        if (rating != FsrsRating.AGAIN) increase = max(increase, 1.0)
        return clampStability(stability * increase)
    }

    private fun nextDifficulty(difficulty: Double, rating: FsrsRating): Double {
        val deltaDifficulty = -(parameters[6] * (rating.value - 3))
        val linearDamping = (10.0 - difficulty) * deltaDifficulty / 9.0
        val easyDifficulty = initialDifficulty(FsrsRating.EASY, clamp = false)
        val nextDifficulty = parameters[7] * easyDifficulty +
            (1 - parameters[7]) * (difficulty + linearDamping)
        return clampDifficulty(nextDifficulty)
    }

    private fun nextStability(
        item: VocabularyItem,
        stability: Double,
        difficulty: Double,
        rating: FsrsRating,
        nowMillis: Long
    ): Double {
        val retrievability = retrievability(
            item = item,
            nowMillis = nowMillis
        ).coerceIn(0.0, 1.0)

        val nextStability = if (rating == FsrsRating.AGAIN) {
            val longTerm = parameters[11] * difficulty.pow(-parameters[12]) *
                ((stability + 1).pow(parameters[13]) - 1) *
                exp((1 - retrievability) * parameters[14])
            val shortTerm = stability / exp(parameters[17] * parameters[18])
            min(longTerm, shortTerm)
        } else {
            val hardPenalty = if (rating == FsrsRating.HARD) parameters[15] else 1.0
            val easyBonus = if (rating == FsrsRating.EASY) parameters[16] else 1.0
            stability * (
                1 + exp(parameters[8]) * (11 - difficulty) * stability.pow(-parameters[9]) *
                    (exp((1 - retrievability) * parameters[10]) - 1) * hardPenalty * easyBonus
            )
        }
        return clampStability(nextStability)
    }

    private fun fuzzInterval(intervalDays: Int): Int {
        if (intervalDays < 3) return intervalDays

        var delta = 1.0
        FUZZ_RANGES.forEach { range ->
            delta += range.factor * max(min(intervalDays.toDouble(), range.end) - range.start, 0.0)
        }
        val minInterval = max(2, (intervalDays - delta).roundToInt())
        val maxInterval = min(maximumIntervalDays, (intervalDays + delta).roundToInt())
        val safeMin = min(minInterval, maxInterval)
        return random.nextInt(safeMin, maxInterval + 1)
    }

    private fun clampStability(stability: Double): Double = max(stability, STABILITY_MIN)

    private fun clampDifficulty(difficulty: Double): Double = difficulty.coerceIn(MIN_DIFFICULTY, MAX_DIFFICULTY)

    private data class Transition(
        val state: FsrsState,
        val step: Int?,
        val delayMillis: Long = 0L,
        val useLongTermInterval: Boolean = false
    )

    private data class FuzzRange(
        val start: Double,
        val end: Double,
        val factor: Double
    )

    companion object {
        private const val MILLIS_PER_MINUTE = 60_000L
        private const val MILLIS_PER_DAY = 24 * 60 * MILLIS_PER_MINUTE
        private const val MINUTE = MILLIS_PER_MINUTE
        private const val STABILITY_MIN = 0.001
        private const val MIN_DIFFICULTY = 1.0
        private const val MAX_DIFFICULTY = 10.0
        private const val DEFAULT_RETENTION = 0.9

        val DEFAULT_PARAMETERS = listOf(
            0.212, 1.2931, 2.3065, 8.2956, 6.4133, 0.8334, 3.0194,
            0.001, 1.8722, 0.1666, 0.796, 1.4835, 0.0614, 0.2629,
            1.6483, 0.6014, 1.8729, 0.5425, 0.0912, 0.0658, 0.1542
        )

        private val FUZZ_RANGES = listOf(
            FuzzRange(start = 2.5, end = 7.0, factor = 0.15),
            FuzzRange(start = 7.0, end = 20.0, factor = 0.1),
            FuzzRange(start = 20.0, end = Double.POSITIVE_INFINITY, factor = 0.05)
        )
    }
}
