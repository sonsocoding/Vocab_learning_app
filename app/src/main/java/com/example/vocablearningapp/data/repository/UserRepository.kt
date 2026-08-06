package com.example.vocablearningapp.data.repository

import com.example.vocablearningapp.data.local.dao.UserDao
import com.example.vocablearningapp.data.local.datastore.UserPreferences
import com.example.vocablearningapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) {
    val currentUserId: Flow<Long?> = userPreferences.currentUserId
    val lastStudiedDeckId: Flow<Long?> = userPreferences.lastStudiedDeckId

    suspend fun loginOrRegister(email: String, displayName: String): Long {
        val existing = userDao.getUserByEmail(email)
        val userId = if (existing != null) {
            existing.id
        } else {
            val newUser = UserEntity(
                email = email.trim(),
                displayName = displayName.trim().ifEmpty { email.substringBefore("@") }
            )
            userDao.insertUser(newUser)
        }
        userPreferences.saveSession(userId)
        return userId
    }

    fun getUser(userId: Long): Flow<UserEntity?> {
        return userDao.getUserByIdFlow(userId)
    }

    suspend fun logout() {
        userPreferences.clearSession()
    }

    suspend fun saveLastStudiedDeck(deckId: Long) {
        userPreferences.saveLastStudiedDeck(deckId)
    }
}
