package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, pass: String, username: String, guitarLevel: String, profilePictureUri: String?): Result<Boolean> {
        return repository.register(email, pass, username, guitarLevel, profilePictureUri)
    }
}
