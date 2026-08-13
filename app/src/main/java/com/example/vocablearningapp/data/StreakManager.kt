package com.example.vocablearningapp.data

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class StreakState(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val isStudiedToday: Boolean = false,
    val activeDaysThisWeek: Set<Int> = emptySet() // 1=Mon, 2=Tue, ..., 7=Sun
)

class StreakManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "vocab_app_streak_prefs"
        private const val KEY_CURRENT_STREAK = "key_current_streak"
        private const val KEY_BEST_STREAK = "key_best_streak"
        private const val KEY_LAST_ACTIVE_DATE = "key_last_active_date"
        private const val KEY_ACTIVE_DATES_SET = "key_active_dates_set"
        private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    fun getStreakState(): StreakState {
        val today = LocalDate.now()
        val todayStr = today.format(DATE_FORMATTER)
        val yesterdayStr = today.minusDays(1).format(DATE_FORMATTER)

        val lastActiveDate = prefs.getString(KEY_LAST_ACTIVE_DATE, null)
        var currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)
        val bestStreak = prefs.getInt(KEY_BEST_STREAK, 0)

        val isStudiedToday = lastActiveDate == todayStr

        if (!isStudiedToday && lastActiveDate != yesterdayStr && lastActiveDate != null) {
            currentStreak = 0
        }

        val startOfWeek = today.minusDays((today.dayOfWeek.value - 1).toLong())
        val activeDatesSet = prefs.getStringSet(KEY_ACTIVE_DATES_SET, emptySet()).orEmpty()

        val activeDaysThisWeek = (1..7).filter { dayOffset ->
            val dateStr = startOfWeek.plusDays((dayOffset - 1).toLong()).format(DATE_FORMATTER)
            dateStr in activeDatesSet
        }.toSet()

        return StreakState(
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            isStudiedToday = isStudiedToday,
            activeDaysThisWeek = activeDaysThisWeek
        )
    }

    fun recordActivityToday(): StreakState {
        val today = LocalDate.now()
        val todayStr = today.format(DATE_FORMATTER)
        val yesterdayStr = today.minusDays(1).format(DATE_FORMATTER)

        val lastActiveDate = prefs.getString(KEY_LAST_ACTIVE_DATE, null)
        var currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)
        var bestStreak = prefs.getInt(KEY_BEST_STREAK, 0)

        if (lastActiveDate == todayStr) {
            return getStreakState()
        }

        currentStreak = if (lastActiveDate == yesterdayStr) {
            currentStreak + 1
        } else {
            1
        }

        if (currentStreak > bestStreak) {
            bestStreak = currentStreak
        }

        val activeDatesSet = prefs.getStringSet(KEY_ACTIVE_DATES_SET, emptySet()).orEmpty().toMutableSet()
        activeDatesSet.add(todayStr)

        prefs.edit()
            .putInt(KEY_CURRENT_STREAK, currentStreak)
            .putInt(KEY_BEST_STREAK, bestStreak)
            .putString(KEY_LAST_ACTIVE_DATE, todayStr)
            .putStringSet(KEY_ACTIVE_DATES_SET, activeDatesSet)
            .apply()

        return getStreakState()
    }
}
