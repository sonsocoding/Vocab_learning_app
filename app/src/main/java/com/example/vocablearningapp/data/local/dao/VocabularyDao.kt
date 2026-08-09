package com.example.vocablearningapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vocablearningapp.data.local.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabularies WHERE deckId = :deckId ORDER BY id ASC")
    fun getVocabulariesByDeck(deckId: Long): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabularies WHERE deckId = :deckId ORDER BY id ASC")
    suspend fun getVocabulariesByDeckList(deckId: Long): List<VocabularyEntity>

    @Query("SELECT COUNT(*) FROM vocabularies WHERE deckId = :deckId")
    fun getVocabularyCountByDeck(deckId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabularies(vocabularies: List<VocabularyEntity>): LongArray
}
