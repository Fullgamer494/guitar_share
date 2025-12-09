package com.example.guitar_share.data.mapper

import com.example.guitar_share.data.model.UserDto
import com.example.guitar_share.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        guitarLevel = guitarLevel,
        profilePictureUrl = profilePictureUrl,
        streakDays = streakDays,
        lastLoginDate = lastLoginDate,
        completedLessons = completedLessons
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        email = email,
        guitarLevel = guitarLevel,
        profilePictureUrl = profilePictureUrl,
        streakDays = streakDays,
        lastLoginDate = lastLoginDate,
        completedLessons = completedLessons
    )
}
