package com.example.vocablearningapp

import android.content.Context
import com.example.vocablearningapp.data.local.database.AppDatabase
import com.example.vocablearningapp.data.local.datastore.UserPreferences
import com.example.vocablearningapp.data.repository.UserRepository
import com.example.vocablearningapp.data.repository.VocabRepository

class AppContainer(private val context: Context) {
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    val userPreferences: UserPreferences by lazy {
        UserPreferences(context)
    }

    val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = database.userDao(),
            userPreferences = userPreferences
        )
    }

    val vocabRepository: VocabRepository by lazy {
        VocabRepository(
            levelDao = database.levelDao(),
            topicDao = database.topicDao(),
            deckDao = database.deckDao(),
            vocabularyDao = database.vocabularyDao(),
            flashcardProgressDao = database.flashcardProgressDao()
        )
    }
}
