package com.example.guitar_share.presentation.ui.screens.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Clase de datos para el estado de la UI del perfil
data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val guitarLevel: String = "",
    val profileImageUrl: String = "https://i.pinimg.com/736x/ba/94/64/ba9464145eba8762f6286a3c8387c951.jpg"
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Al iniciar el ViewModel, cargamos los datos del usuario (ahora simulados)
        loadUserData()
    }

    private fun loadUserData() {
        // Como no hay Firebase, usamos datos fijos para iniciar.
        _uiState.update { currentState ->
            currentState.copy(
                username = "Fullgamer494",
                email = "fullgamer494@hotmail.com",
                guitarLevel = "Principiante"
            )
        }
    }

    // Función que EditProfileScreen llamará para guardar los cambios
    fun saveProfileChanges(newUsername: String, newEmail: String, newGuitarLevel: String) {
        // viewModelScope.launch se usa para operaciones largas, pero aquí es instantáneo.
        // Es una buena práctica mantenerlo para cuando agregues Firebase.
        viewModelScope.launch {
            // --- Lógica de guardado SIMULADA ---
            // En lugar de llamar a Firebase, solo actualizamos el estado en la app.
            // Esto hará que ProfileScreen se refresque con los nuevos datos.
            _uiState.update { currentState ->
                currentState.copy(
                    username = newUsername,
                    email = newEmail,
                    guitarLevel = newGuitarLevel
                )
            }
        }
    }
}
