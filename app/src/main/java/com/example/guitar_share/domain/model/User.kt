package com.example.guitar_share.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val guitarLevel: String,
    val profilePictureUrl: String,
    val streakDays: Int = 0,
    val lastLoginDate: Long = 0,
    val completedLessons: List<String> = emptyList()
)
