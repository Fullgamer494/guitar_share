package com.example.guitar_share.data.repository

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserId(): String?
    suspend fun login(email: String, pass: String): Result<Boolean>
    suspend fun register(email: String, pass: String, username: String): Result<Boolean>

    suspend fun getUserData(uid: String): Result<com.example.guitar_share.data.model.UserDto>
    fun logout()
}