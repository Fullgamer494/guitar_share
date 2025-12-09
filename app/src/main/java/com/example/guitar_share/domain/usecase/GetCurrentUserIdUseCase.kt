package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class GetCurrentUserIdUseCase(private val repository: AuthRepository) {
    operator fun invoke(): String? {
        return repository.getCurrentUserId()
    }
}
