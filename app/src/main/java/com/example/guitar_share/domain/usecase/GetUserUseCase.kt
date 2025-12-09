package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.model.User
import com.example.guitar_share.domain.repository.AuthRepository

class GetUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(uid: String): Result<User> {
        return repository.getUserData(uid)
    }
}
