package com.example.vocablearningapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vocablearningapp.data.local.entity.FlashcardProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: FlashcardProgressEntity)

    @Query("""
        SELECT fp.* FROM flashcard_progress fp
        INNER JOIN vocabularies v ON fp.vocabularyId = v.id
        WHERE fp.userId = :userId AND v.deckId = :deckId
    """)
    fun getProgressForUserAndDeck(userId: Long, deckId: Long): Flow<List<FlashcardProgressEntity>>

    @Query("""
        SELECT fp.* FROM flashcard_progress fp
        INNER JOIN vocabularies v ON fp.vocabularyId = v.id
        WHERE fp.userId = :userId AND v.deckId = :deckId
    """)
    suspend fun getProgressForUserAndDeckList(userId: Long, deckId: Long): List<FlashcardProgressEntity>

    @Query("SELECT * FROM flashcard_progress WHERE userId = :userId AND vocabularyId = :vocabularyId LIMIT 1")
    suspend fun getProgressForUserAndVocab(userId: Long, vocabularyId: Long): FlashcardProgressEntity?

    @Query("SELECT COUNT(DISTINCT vocabularyId) FROM flashcard_progress WHERE userId = :userId")
    fun getTotalLearnedCount(userId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcard_progress WHERE userId = :userId")
    fun getTotalRatedCount(userId: Long): Flow<Int>
}
