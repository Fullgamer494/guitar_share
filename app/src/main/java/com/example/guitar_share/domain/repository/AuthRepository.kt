package com.example.guitar_share.domain.repository

import com.example.guitar_share.domain.model.User

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserId(): String?
    suspend fun login(email: String, pass: String): Result<Boolean>
    suspend fun register(email: String, pass: String, username: String, guitarLevel: String, profilePictureUri: String?): Result<Boolean>
    suspend fun updateUser(uid: String, username: String, email: String, profilePictureUri: String?): Result<Boolean>
    suspend fun checkUpdateStreak(uid: String): Result<Boolean>
    suspend fun markLessonCompleted(uid: String, lessonTitle: String): Result<Boolean>
    suspend fun getUserData(uid: String): Result<User>
    fun logout()
}
