package com.example.guitar_share.presentation.screens.scores_library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class ScoreLibraryViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var allSongs = emptyList<ScoreLibrarySong>()
    private val _songs = MutableStateFlow<List<ScoreLibrarySong>>(emptyList())
    val songs: StateFlow<List<ScoreLibrarySong>> = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Search Logic
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchSuggestions = MutableStateFlow<List<String>>(emptyList())
    val searchSuggestions: StateFlow<List<String>> = _searchSuggestions.asStateFlow()

    init {
        viewModelScope.launch {
            combine(_songs, _searchQuery) { songs, query ->
                if (query.isBlank()) {
                    emptyList()
                } else {
                    val suggestions = mutableSetOf<String>()
                    songs.forEach { song ->
                        if (song.title.contains(query, ignoreCase = true)) suggestions.add(song.title)
                        if (song.artist.contains(query, ignoreCase = true)) suggestions.add(song.artist)
                        song.tags.forEach { tag ->
                            if (tag.contains(query, ignoreCase = true)) suggestions.add(tag)
                        }
                    }
                    suggestions.toList().take(5)
                }
            }.collect { suggestions ->
                _searchSuggestions.value = suggestions
            }
        }
    }

    fun fetchUserScores(userId: String) {
        _isLoading.value = true
        db.collection("scores")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                _isLoading.value = false
                if (e != null || snapshot == null) return@addSnapshotListener

                val songsList = snapshot.documents.mapNotNull { doc ->
                    try {
                        ScoreLibrarySong(
                            id = doc.id,
                            userId = doc.getString("userId") ?: "",
                            title = doc.getString("title") ?: "",
                            artist = doc.getString("artist") ?: "",
                            genre = doc.getString("genre") ?: "",
                            key = doc.getString("key") ?: "",
                            coverUrl = doc.getString("coverUrl") ?: "",
                            tags = (doc.get("tags") as? List<String>) ?: emptyList()
                        )
                    } catch (e: Exception) { null }
                }
                allSongs = songsList
                onSearchQueryChanged(_searchQuery.value)
            }
    }

    fun uploadScore(
        title: String,
        artist: String,
        genre: String,
        key: String,
        imageUri: Uri?,
        userId: String,
        tags: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (imageUri == null) {
            onError("Debes seleccionar una imagen para la partitura")
            return
        }

        _isLoading.value = true
        val filename = UUID.randomUUID().toString()
        val ref = storage.reference.child("score_covers/$filename")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveScoreToFirestore(title, artist, genre, key, downloadUrl.toString(), userId, tags, onSuccess, onError)
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Error al subir imagen")
            }
    }

    private fun saveScoreToFirestore(
        title: String, artist: String, genre: String, key: String, imageUrl: String,
        userId: String, tags: List<String>,
        onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        val scoreData = hashMapOf(
            "userId" to userId,
            "title" to title,
            "artist" to artist,
            "genre" to genre,
            "key" to key,
            "coverUrl" to imageUrl,
            "tags" to tags,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("scores")
            .add(scoreData)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Error al guardar datos")
            }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _songs.value = allSongs
        } else {
            _songs.value = allSongs.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                        song.artist.contains(query, ignoreCase = true) ||
                        song.tags.any { it.contains(query, ignoreCase = true) }
            }
        }
    }
    
    fun getSongById(id: String): ScoreLibrarySong? {
        return allSongs.find { it.id == id }
    }
}
