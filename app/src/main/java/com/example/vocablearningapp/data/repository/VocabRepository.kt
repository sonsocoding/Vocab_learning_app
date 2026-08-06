package com.example.vocablearningapp.data.repository

import com.example.vocablearningapp.data.local.dao.DeckDao
import com.example.vocablearningapp.data.local.dao.FlashcardProgressDao
import com.example.vocablearningapp.data.local.dao.LevelDao
import com.example.vocablearningapp.data.local.dao.TopicDao
import com.example.vocablearningapp.data.local.dao.VocabularyDao
import com.example.vocablearningapp.data.local.entity.DeckEntity
import com.example.vocablearningapp.data.local.entity.FlashcardProgressEntity
import com.example.vocablearningapp.data.local.entity.LevelEntity
import com.example.vocablearningapp.data.local.entity.TopicEntity
import com.example.vocablearningapp.domain.model.DeckWithProgress
import com.example.vocablearningapp.domain.model.FlashcardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class VocabRepository(
    private val levelDao: LevelDao,
    private val topicDao: TopicDao,
    private val deckDao: DeckDao,
    private val vocabularyDao: VocabularyDao,
    private val flashcardProgressDao: FlashcardProgressDao
) {
    fun getLevels(): Flow<List<LevelEntity>> = levelDao.getAllLevels()

    fun getTopicsByLevel(levelId: String): Flow<List<TopicEntity>> = topicDao.getTopicsByLevel(levelId)

    suspend fun getTopicById(topicId: Long): TopicEntity? = topicDao.getTopicById(topicId)

    fun getDecksByTopic(userId: Long, topicId: Long): Flow<List<DeckWithProgress>> {
        val decksFlow = deckDao.getDecksByTopic(topicId)
        return combine(decksFlow) { array ->
            val decks = array.firstOrNull() ?: emptyList()
            decks.map { deck ->
                val vocabs = vocabularyDao.getVocabulariesByDeckList(deck.id)
                val progressList = flashcardProgressDao.getProgressForUserAndDeckList(userId, deck.id)
                DeckWithProgress(
                    deck = deck,
                    totalWords = vocabs.size,
                    learnedWords = progressList.size
                )
            }
        }
    }

    suspend fun getDeckById(deckId: Long): DeckEntity? = deckDao.getDeckById(deckId)

    suspend fun getDeckWithProgress(userId: Long, deckId: Long): DeckWithProgress? {
        val deck = deckDao.getDeckById(deckId) ?: return null
        val vocabs = vocabularyDao.getVocabulariesByDeckList(deckId)
        val progressList = flashcardProgressDao.getProgressForUserAndDeckList(userId, deckId)
        return DeckWithProgress(
            deck = deck,
            totalWords = vocabs.size,
            learnedWords = progressList.size
        )
    }

    suspend fun getFlashcardItemsForDeck(userId: Long, deckId: Long): List<FlashcardItem> {
        val vocabs = vocabularyDao.getVocabulariesByDeckList(deckId)
        val progressMap = flashcardProgressDao.getProgressForUserAndDeckList(userId, deckId)
            .associateBy { it.vocabularyId }

        return vocabs.map { vocab ->
            FlashcardItem(
                vocabulary = vocab,
                progress = progressMap[vocab.id]
            )
        }
    }

    suspend fun updateFlashcardProgress(userId: Long, vocabularyId: Long, memoryLevel: Int) {
        val existing = flashcardProgressDao.getProgressForUserAndVocab(userId, vocabularyId)
        val newProgress = if (existing != null) {
            existing.copy(
                memoryLevel = memoryLevel,
                reviewCount = existing.reviewCount + 1,
                lastReviewedAt = System.currentTimeMillis()
            )
        } else {
            FlashcardProgressEntity(
                userId = userId,
                vocabularyId = vocabularyId,
                memoryLevel = memoryLevel,
                reviewCount = 1,
                lastReviewedAt = System.currentTimeMillis()
            )
        }
        flashcardProgressDao.upsertProgress(newProgress)
    }

    fun getTotalLearnedCount(userId: Long): Flow<Int> = flashcardProgressDao.getTotalLearnedCount(userId)

    fun getTotalRatedCount(userId: Long): Flow<Int> = flashcardProgressDao.getTotalRatedCount(userId)
}
