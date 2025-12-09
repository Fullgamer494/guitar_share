package com.example.guitar_share.presentation.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitar_share.domain.model.User
import com.example.guitar_share.domain.usecase.GetCurrentUserIdUseCase
import com.example.guitar_share.domain.usecase.GetUserUseCase
import com.example.guitar_share.domain.usecase.IsUserLoggedInUseCase
import com.example.guitar_share.domain.usecase.LoginUseCase
import com.example.guitar_share.domain.usecase.LogoutUseCase
import com.example.guitar_share.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val updateUserUseCase: com.example.guitar_share.domain.usecase.UpdateUserUseCase,
    private val checkStreakUseCase: com.example.guitar_share.domain.usecase.CheckStreakUseCase,
    private val markLessonCompletedUseCase: com.example.guitar_share.domain.usecase.MarkLessonCompletedUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var navigateToHome by mutableStateOf(isUserLoggedInUseCase())

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = loginUseCase(email, pass)
            if (result.isSuccess) {
                navigateToHome = true
            } else {
                errorMessage = "Error: ${result.exceptionOrNull()?.message}"
            }
            isLoading = false
        }
    }

    fun register(email: String, pass: String, username: String, guitarLevel: String, profilePictureUri: String?) {
        if (username.isBlank()) {
            errorMessage = "El usuario es obligatorio"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = registerUseCase(email, pass, username, guitarLevel, profilePictureUri)
            if (result.isSuccess) {
                navigateToHome = true
            } else {
                errorMessage = "Error: ${result.exceptionOrNull()?.message}"
            }
            isLoading = false
        }
    }

    var currentUserData by mutableStateOf<User?>(null)

    fun fetchCurrentUser() {
        val uid = getCurrentUserIdUseCase()
        if (uid != null) {
            viewModelScope.launch {
                // Check streak first or in parallel, but we want the updated data
                checkStreakUseCase(uid)
                
                val result = getUserUseCase(uid)
                if (result.isSuccess) {
                    currentUserData = result.getOrNull()
                }
            }
        }
    }

    fun updateUser(username: String, email: String, profilePictureUri: String?, onSuccess: () -> Unit) {
        val uid = getCurrentUserIdUseCase() ?: return
        viewModelScope.launch {
            isLoading = true
            val result = updateUserUseCase(uid, username, email, profilePictureUri)
            if (result.isSuccess) {
                fetchCurrentUser() // Refresh data
                onSuccess()
            } else {
                errorMessage = "Error al actualizar: ${result.exceptionOrNull()?.message}"
            }
            isLoading = false
        }
    }

    fun markLessonCompleted(lessonTitle: String) {
        val uid = getCurrentUserIdUseCase() ?: return
        // Optimistic update
        val current = currentUserData
        if (current != null && !current.completedLessons.contains(lessonTitle)) {
            currentUserData = current.copy(completedLessons = current.completedLessons + lessonTitle)
            
            viewModelScope.launch {
                markLessonCompletedUseCase(uid, lessonTitle)
            }
        }
    }

    fun logout() {
        logoutUseCase()
        navigateToHome = false
    }
}
