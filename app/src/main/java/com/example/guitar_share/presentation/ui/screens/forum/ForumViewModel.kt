package com.example.guitar_share.presentation.ui.screens.forum

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ForumViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
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
                    _error.value = error.message
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

    fun createPostWithImage(
        title: String,
        body: String,
        authorName: String,
        tag: String,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true

        if (imageUri != null) {
            val filename = UUID.randomUUID().toString()
            val ref = storage.reference.child("post_images/$filename")

            ref.putFile(imageUri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                        savePostToFirestore(title, body, authorName, tag, downloadUrl.toString(), onSuccess, onError)
                    }.addOnFailureListener { e ->
                        _isLoading.value = false
                        onError(e.message ?: "Error desconocido")
                    }
                }
                .addOnFailureListener { e ->
                    _isLoading.value = false
                    onError(e.message ?: "Error desconocido")
                }
        } else {
            savePostToFirestore(title, body, authorName, tag, null, onSuccess, onError)
        }
    }

    private fun savePostToFirestore(
        title: String,
        body: String,
        authorName: String,
        tag: String,
        imageUrl: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val post = hashMapOf(
            "title" to title,
            "body" to body,
            "authorName" to authorName,
            "tag" to tag,
            "timeAgo" to "Hace un momento",
            "likesCount" to 0,
            "commentsCount" to 0,
            "timestamp" to Timestamp.now(),
            "imageUrl" to imageUrl
        )

        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError(e.message ?: "Error desconocido")
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}