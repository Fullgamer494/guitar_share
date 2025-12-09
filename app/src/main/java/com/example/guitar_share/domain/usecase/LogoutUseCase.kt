package com.example.guitar_share.domain.usecase

import com.example.guitar_share.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    operator fun invoke() {
        repository.logout()
    }
}
