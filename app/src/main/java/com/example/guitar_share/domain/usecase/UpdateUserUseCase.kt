package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class UpdateUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(uid: String, username: String, email: String, profilePictureUri: String?): Result<Boolean> {
        return repository.updateUser(uid, username, email, profilePictureUri)
    }
}
