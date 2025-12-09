package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class IsUserLoggedInUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}
