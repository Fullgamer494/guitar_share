package com.example.guitar_share.presentation.screens.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitar_share.data.repository.LessonRepository
import com.example.guitar_share.domain.model.LessonContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonViewModel(private val repository: LessonRepository) : ViewModel() {

    private val _state = MutableStateFlow<LessonState>(LessonState.Loading)
    val state: StateFlow<LessonState> = _state.asStateFlow()

    fun loadLesson(title: String) {
        viewModelScope.launch {
            _state.value = LessonState.Loading
            try {
                val content = repository.getLessonContent(title)
                _state.value = LessonState.Success(content)
            } catch (e: Exception) {
                _state.value = LessonState.Error("Error al cargar lección: ${e.message}")
            }
        }
    }
}

sealed class LessonState {
    object Loading : LessonState()
    data class Success(val content: LessonContent) : LessonState()
    data class Error(val message: String) : LessonState()
}
