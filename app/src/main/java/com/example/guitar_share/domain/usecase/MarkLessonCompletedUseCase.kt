package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class MarkLessonCompletedUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userId: String, lessonTitle: String): Result<Boolean> {
        return repository.markLessonCompleted(userId, lessonTitle)
    }
}
