package com.example.vocablearningapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vocablearningapp.data.local.entity.SetWithWords
import com.example.vocablearningapp.data.local.entity.VocabularyItemEntity
import com.example.vocablearningapp.data.local.entity.VocabularySetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {

    @Transaction
    @Query("SELECT * FROM vocabulary_sets ORDER BY createdAtMillis ASC")
    fun getAllSetsWithWordsFlow(): Flow<List<SetWithWords>>

    @Transaction
    @Query("SELECT * FROM vocabulary_sets ORDER BY createdAtMillis ASC")
    fun getAllSetsWithWords(): List<SetWithWords>

    @Transaction
    @Query("SELECT * FROM vocabulary_sets WHERE id = :setId")
    fun getSetWithWordsById(setId: String): SetWithWords?

    @Query("SELECT * FROM vocabulary_items WHERE id = :itemId")
    fun getItemById(itemId: String): VocabularyItemEntity?

    @Query("SELECT * FROM vocabulary_items")
    fun getAllItems(): List<VocabularyItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSet(set: VocabularySetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSets(sets: List<VocabularySetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<VocabularyItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: VocabularyItemEntity)

    @Update
    fun updateItem(item: VocabularyItemEntity)

    @Query("DELETE FROM vocabulary_items WHERE setId = :setId")
    fun deleteItemsBySetId(setId: String): Int

    @Query("DELETE FROM vocabulary_sets WHERE id = :setId")
    fun deleteSetById(setId: String): Int

    @Query("SELECT COUNT(*) FROM vocabulary_sets")
    fun getSetCount(): Int
}
