package com.example.vocablearningapp

import com.example.vocablearningapp.data.MockData
import com.example.vocablearningapp.domain.model.MemoryLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockDataTest {
    @Test
    fun studySetsStartMostlyForgotWithOnlyThreeLearningWords() {
        val items = MockData.vocabularySets.flatMap { it.words }

        assertEquals(0, items.count { it.memoryLevel == MemoryLevel.MASTERED })
        assertEquals(3, items.count { it.memoryLevel == MemoryLevel.LEARNING })
        assertEquals(items.size - 3, items.count { it.memoryLevel == MemoryLevel.FORGOT })
    }

    @Test
    fun dailyWordsAreAvailableForEveryCefrLevel() {
        assertEquals(setOf("A1", "A2", "B1", "B2", "C1", "C2"), MockData.dailyWordsByLevel.keys)
        assertTrue(MockData.dailyWordsByLevel.values.all { it.isNotEmpty() })
    }
}
