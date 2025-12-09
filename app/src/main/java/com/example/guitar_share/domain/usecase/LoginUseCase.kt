package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, pass: String): Result<Boolean> {
        return repository.login(email, pass)
    }
}
