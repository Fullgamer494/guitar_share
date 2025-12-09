package com.example.guitar_share.presentation.screens.comments

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CommentsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    private val _comments = MutableStateFlow<List<CommentData>>(emptyList())
    val comments: StateFlow<List<CommentData>> = _comments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun listenToComments(postId: String) {
        _isLoading.value = true
        listenerRegistration?.remove()

        listenerRegistration = db.collection("posts").document(postId).collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) {
                    _error.value = error.message
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val commentsList = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(CommentData::class.java)?.copy(id = doc.id)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _comments.value = commentsList
                }
            }
    }

    fun addComment(
        postId: String,
        userId: String,
        authorName: String,
        authorProfileUrl: String?,
        text: String,
        imageUri: Uri?,
        parentId: String? = null,
        targetUserId: String? = null, // User to notify
        onSuccess: () -> Unit
    ) {
        if (text.isBlank() && imageUri == null) return

        _isLoading.value = true

        if (imageUri != null) {
            val filename = UUID.randomUUID().toString()
            val ref = storage.reference.child("comment_images/$filename")

            ref.putFile(imageUri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                        saveCommentToFirestore(postId, userId, authorName, authorProfileUrl, text, downloadUrl.toString(), parentId, targetUserId, onSuccess)
                    }
                }
                .addOnFailureListener {
                    _isLoading.value = false
                    _error.value = "Error al subir imagen: ${it.message}"
                }
        } else {
            saveCommentToFirestore(postId, userId, authorName, authorProfileUrl, text, null, parentId, targetUserId, onSuccess)
        }
    }

    private fun saveCommentToFirestore(
        postId: String,
        userId: String,
        authorName: String,
        authorProfileUrl: String?,
        text: String,
        imageUrl: String?,
        parentId: String?,
        targetUserId: String?,
        onSuccess: () -> Unit
    ) {
        val newComment = CommentData(
            postId = postId,
            userId = userId,
            authorName = authorName,
            authorProfileUrl = authorProfileUrl,
            text = text,
            imageUrl = imageUrl,
            timestamp = Timestamp.now(),
            parentId = parentId
        )

        db.collection("posts").document(postId).collection("comments")
            .add(newComment)
            .addOnSuccessListener {
                // Increment comments count on the post
                db.collection("posts").document(postId)
                    .update("commentsCount", FieldValue.increment(1))

                // Send notification if targetUserId exists and it's not the same user
                if (targetUserId != null && targetUserId != userId) {
                    val notification = hashMapOf(
                        "fromUserId" to userId,
                        "fromUserName" to authorName,
                        "fromUserProfileUrl" to authorProfileUrl,
                        "type" to "reply",
                        "postId" to postId,
                        "commentId" to it.id,
                        "timestamp" to Timestamp.now(),
                        "read" to false,
                        "targetUserId" to targetUserId // Indexing helper
                    )
                    db.collection("users").document(targetUserId).collection("notifications")
                        .add(notification)
                }

                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener {
                _isLoading.value = false
                _error.value = "Error al publicar comentario: ${it.message}"
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
