package com.example.vocablearningapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_USER_ID = longPreferencesKey("logged_in_user_id")
        private val KEY_LAST_DECK_ID = longPreferencesKey("last_studied_deck_id")
    }

    val currentUserId: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_ID]
    }

    val lastStudiedDeckId: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[KEY_LAST_DECK_ID]
    }

    suspend fun saveSession(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_LAST_DECK_ID)
        }
    }

    suspend fun saveLastStudiedDeck(deckId: Long) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LAST_DECK_ID] = deckId
        }
    }
}
