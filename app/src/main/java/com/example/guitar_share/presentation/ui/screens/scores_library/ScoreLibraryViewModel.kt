package com.example.guitar_share.presentation.ui.screens.scores_library

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreLibraryViewModel : ViewModel() {

    private var allSongs = emptyList<ScoreLibrarySong>()

    private val _songs = MutableStateFlow<List<ScoreLibrarySong>>(emptyList())
    val songs: StateFlow<List<ScoreLibrarySong>> = _songs.asStateFlow()

    init {
        loadDummyData()
    }

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            _songs.value = allSongs
        } else {
            _songs.value = allSongs.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                        song.artist.contains(query, ignoreCase = true)
            }
        }
    }

    private fun loadDummyData() {
        val dummyData = listOf(
            ScoreLibrarySong(1, "Lamento Boliviano", "Enanitos Verdes", "https://upload.wikimedia.org/wikipedia/en/6/64/Enanitos_Verdes_-_Big_Bang.jpg"),
            ScoreLibrarySong(2, "De Música Ligera", "Soda Stereo", "https://upload.wikimedia.org/wikipedia/en/2/2e/Cancion_animal.jpg"),
            ScoreLibrarySong(3, "Persiana Americana", "Soda Stereo", "https://upload.wikimedia.org/wikipedia/en/3/37/Signos_Soda_Stereo.jpg"),
            ScoreLibrarySong(4, "Rayando el Sol", "Maná", "https://upload.wikimedia.org/wikipedia/en/0/0f/Mana_falta_amor.jpg"),
            ScoreLibrarySong(5, "La Muralla Verde", "Enanitos Verdes", "https://upload.wikimedia.org/wikipedia/en/3/39/Contrareloj.jpg")
        )

        allSongs = dummyData
        _songs.value = dummyData
    }
}