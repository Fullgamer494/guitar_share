package com.example.guitar_share.presentation.ui.screens.forum

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForumViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    private val _posts = MutableStateFlow<List<ForumPostData>>(emptyList())
    val posts: StateFlow<List<ForumPostData>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        listenToPosts()
    }

    private fun listenToPosts() {
        _isLoading.value = true

        listenerRegistration = db.collection("posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false

                if (error != null) {
                    _error.value = "Error al cargar posts: ${error.message}"
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val postsList = snapshot.documents.mapNotNull { doc ->
                        try {
                            ForumPostData(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                body = doc.getString("body") ?: "",
                                authorName = doc.getString("authorName") ?: "Anónimo",
                                profilePictureUrl = doc.getString("profilePictureUrl"),
                                timeAgo = doc.getString("timeAgo") ?: "Hace un momento",
                                tag = doc.getString("tag") ?: "General",
                                likesCount = doc.getLong("likesCount")?.toInt() ?: 0,
                                commentsCount = doc.getLong("commentsCount")?.toInt() ?: 0,
                                imageUrl = doc.getString("imageUrl")
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _posts.value = postsList
                }
            }
    }

    fun createPost(
        title: String,
        body: String,
        authorName: String,
        tag: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (title.isBlank() || body.isBlank()) {
            onError("El título y el cuerpo no pueden estar vacíos")
            return
        }

        val post = hashMapOf(
            "title" to title,
            "body" to body,
            "authorName" to authorName,
            "tag" to tag,
            "timeAgo" to "Hace un momento",
            "likesCount" to 0,
            "commentsCount" to 0,
            "timestamp" to com.google.firebase.Timestamp.now(),
            "imageUrl" to imageUrl
        )

        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError("Error al crear post: ${e.message}")
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}