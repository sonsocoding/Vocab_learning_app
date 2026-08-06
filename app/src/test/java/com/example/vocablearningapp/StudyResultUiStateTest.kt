package com.example.vocablearningapp

import com.example.vocablearningapp.ui.screen.result.StudyResultUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class StudyResultUiStateTest {

    @Test
    fun testRememberedRatioCalculation() {
        val state = StudyResultUiState(
            totalWords = 10,
            hardCount = 1,
            somewhatCount = 2,
            rememberedCount = 4,
            veryWellCount = 3
        )
        // (4 + 3) / 10 = 70%
        assertEquals(70, state.rememberedRatio)
    }

    @Test
    fun testRememberedRatioZeroTotalWords() {
        val state = StudyResultUiState(
            totalWords = 0,
            hardCount = 0,
            somewhatCount = 0,
            rememberedCount = 0,
            veryWellCount = 0
        )
        assertEquals(0, state.rememberedRatio)
    }
}
