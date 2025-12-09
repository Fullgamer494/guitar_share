package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class CheckStreakUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(uid: String): Result<Boolean> {
        return repository.checkUpdateStreak(uid)
    }
}
