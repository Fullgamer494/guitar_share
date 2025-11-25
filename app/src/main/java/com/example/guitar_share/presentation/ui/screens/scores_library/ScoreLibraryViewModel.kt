package com.example.guitar_share.presentation.ui.screens.scores_library

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ScoreLibraryViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var allSongs = emptyList<ScoreLibrarySong>()
    private val _songs = MutableStateFlow<List<ScoreLibrarySong>>(emptyList())
    val songs: StateFlow<List<ScoreLibrarySong>> = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSongsFromFirestore()
    }

    private fun loadSongsFromFirestore() {
        _isLoading.value = true
        db.collection("scores")
            .addSnapshotListener { snapshot, e ->
                _isLoading.value = false
                if (e != null || snapshot == null) return@addSnapshotListener

                val songsList = snapshot.documents.mapNotNull { doc ->
                    try {
                        ScoreLibrarySong(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            artist = doc.getString("artist") ?: "",
                            genre = doc.getString("genre") ?: "",
                            key = doc.getString("key") ?: "",
                            coverUrl = doc.getString("coverUrl") ?: ""
                        )
                    } catch (e: Exception) { null }
                }
                allSongs = songsList
                _songs.value = songsList
            }
    }

    fun uploadScore(
        title: String,
        artist: String,
        genre: String,
        key: String,
        imageUri: Uri?,
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
                    saveScoreToFirestore(title, artist, genre, key, downloadUrl.toString(), onSuccess, onError)
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                onError(it.message ?: "Error al subir imagen")
            }
    }

    private fun saveScoreToFirestore(
        title: String, artist: String, genre: String, key: String, imageUrl: String,
        onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        val scoreData = hashMapOf(
            "title" to title,
            "artist" to artist,
            "genre" to genre,
            "key" to key,
            "coverUrl" to imageUrl,
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
        if (query.isBlank()) {
            _songs.value = allSongs
        } else {
            _songs.value = allSongs.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                        song.artist.contains(query, ignoreCase = true)
            }
        }
    }
}