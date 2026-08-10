package com.example.vocablearningapp

import com.example.vocablearningapp.data.MockData
import com.example.vocablearningapp.domain.model.FsrsState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockDataTest {
    @Test
    fun studySetsStartMostlyNewWithOnlyThreeLearningWords() {
        val items = MockData.vocabularySets.flatMap { it.words }

        assertEquals(0, items.count { it.fsrsState == FsrsState.REVIEW })
        assertEquals(3, items.count { it.fsrsState == FsrsState.LEARNING })
        assertEquals(items.size - 3, items.count { it.fsrsState == FsrsState.NEW })
    }

    @Test
    fun vocabularyItemsHaveVietnameseMeaningIpaPartOfSpeechAndExample() {
        val items = MockData.vocabularySets.flatMap { it.words } +
            MockData.dailyWordsByLevel.values.flatten()

        assertTrue(items.all { it.meaning.isNotBlank() })
        assertTrue(items.all { it.pronunciation.isNotBlank() })
        assertTrue(items.all { it.exampleSentence.isNotBlank() })
        assertTrue(items.all { it.partOfSpeech.label in setOf("noun", "verb", "adj") })
    }

    @Test
    fun dailyWordsAreAvailableForEveryCefrLevel() {
        assertEquals(setOf("A1", "A2", "B1", "B2", "C1", "C2"), MockData.dailyWordsByLevel.keys)
        assertTrue(MockData.dailyWordsByLevel.values.all { it.isNotEmpty() })
    }
}
