package com.example.vocablearningapp.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.example.vocablearningapp.data.MockData
import com.example.vocablearningapp.data.local.AppDatabase
import com.example.vocablearningapp.data.local.entity.toDomainModel
import com.example.vocablearningapp.data.local.entity.toEntity
import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.domain.srs.FsrsSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VocabularyRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val dao = database.vocabularyDao()

    suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        val count = dao.getSetCount()
        if (count == 0) {
            val initialSets = MockData.vocabularySets
            val setEntities = initialSets.map { it.toEntity() }
            val itemEntities = initialSets.flatMap { set ->
                set.words.mapIndexed { index, item -> item.toEntity(set.id, index) }
            }
            database.withTransaction {
                dao.insertSets(setEntities)
                dao.insertItems(itemEntities)
            }
        }
    }

    fun getAllSetsFlow(): Flow<List<VocabularySet>> {
        return dao.getAllSetsWithWordsFlow().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    suspend fun getAllSets(): List<VocabularySet> = withContext(Dispatchers.IO) {
        dao.getAllSetsWithWords().map { it.toDomainModel() }
    }

    suspend fun getSetById(setId: String): VocabularySet? = withContext(Dispatchers.IO) {
        dao.getSetWithWordsById(setId)?.toDomainModel()
    }

    suspend fun saveSet(set: VocabularySet) = withContext(Dispatchers.IO) {
        val setEntity = set.toEntity()
        val itemEntities = set.words.mapIndexed { index, item -> item.toEntity(set.id, index) }
        database.withTransaction {
            dao.insertSet(setEntity)
            dao.deleteItemsBySetId(set.id)
            dao.insertItems(itemEntities)
        }
    }

    suspend fun deleteSet(setId: String) = withContext(Dispatchers.IO) {
        database.withTransaction {
            dao.deleteItemsBySetId(setId)
            dao.deleteSetById(setId)
        }
    }

    suspend fun updateWordFsrs(
        itemId: String,
        schedule: FsrsSchedule,
        rating: FsrsRating,
        nowMillis: Long
    ): VocabularyItem? = withContext(Dispatchers.IO) {
        val existing = dao.getItemById(itemId) ?: return@withContext null
        val isLapse = existing.fsrsState == FsrsState.REVIEW.name && rating == FsrsRating.AGAIN
        val updated = existing.copy(
            fsrsState = schedule.state.name,
            fsrsStep = schedule.step,
            stability = schedule.stability,
            difficulty = schedule.difficulty,
            dueAtMillis = schedule.dueAtMillis,
            lastReviewAtMillis = nowMillis,
            scheduledDays = schedule.scheduledDays,
            reviewCount = existing.reviewCount + 1,
            lapseCount = existing.lapseCount + (if (isLapse) 1 else 0)
        )
        dao.updateItem(updated)
        updated.toDomainModel()
    }
}
