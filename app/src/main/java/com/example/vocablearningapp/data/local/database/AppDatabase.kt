package com.example.vocablearningapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vocablearningapp.data.local.dao.DeckDao
import com.example.vocablearningapp.data.local.dao.FlashcardProgressDao
import com.example.vocablearningapp.data.local.dao.LevelDao
import com.example.vocablearningapp.data.local.dao.TopicDao
import com.example.vocablearningapp.data.local.dao.UserDao
import com.example.vocablearningapp.data.local.dao.VocabularyDao
import com.example.vocablearningapp.data.local.entity.DeckEntity
import com.example.vocablearningapp.data.local.entity.FlashcardProgressEntity
import com.example.vocablearningapp.data.local.entity.LevelEntity
import com.example.vocablearningapp.data.local.entity.TopicEntity
import com.example.vocablearningapp.data.local.entity.UserEntity
import com.example.vocablearningapp.data.local.entity.VocabularyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        LevelEntity::class,
        TopicEntity::class,
        DeckEntity::class,
        VocabularyEntity::class,
        FlashcardProgressEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun levelDao(): LevelDao
    abstract fun topicDao(): TopicDao
    abstract fun deckDao(): DeckDao
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun flashcardProgressDao(): FlashcardProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vocab_learning_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                DatabaseSeeder.seedDatabase(
                                    database.levelDao(),
                                    database.topicDao(),
                                    database.deckDao(),
                                    database.vocabularyDao()
                                )
                            }
                        }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                DatabaseSeeder.seedDatabase(
                                    database.levelDao(),
                                    database.topicDao(),
                                    database.deckDao(),
                                    database.vocabularyDao()
                                )
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
