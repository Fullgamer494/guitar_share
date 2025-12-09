package com.example.guitar_share.presentation.screens.forum

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
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

    // Search Logic
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchSuggestions = MutableStateFlow<List<String>>(emptyList())
    val searchSuggestions: StateFlow<List<String>> = _searchSuggestions.asStateFlow()

    init {
        listenToPosts()
        
        // Observe posts and query to update suggestions
        viewModelScope.launch {
            combine(_posts, _searchQuery) { posts, query ->
                if (query.isBlank()) {
                    emptyList()
                } else {
                    val suggestions = mutableSetOf<String>()
                    posts.forEach { post ->
                        if (post.title.contains(query, ignoreCase = true)) suggestions.add(post.title)
                        if (post.authorName.contains(query, ignoreCase = true)) suggestions.add(post.authorName)
                        if (post.tag.contains(query, ignoreCase = true)) suggestions.add(post.tag)
                    }
                    suggestions.toList().take(5) // Limit to 5 suggestions
                }
            }.collect { suggestions ->
                _searchSuggestions.value = suggestions
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun listenToPosts() {
        _isLoading.value = true

        listenerRegistration = db.collection("posts")
            .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
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
                                userId = doc.getString("userId") ?: "",
                                title = doc.getString("title") ?: "",
                                body = doc.getString("body") ?: "",
                                authorName = doc.getString("authorName") ?: "Anónimo",
                                profilePictureUrl = doc.getString("authorProfileUrl"), // Changed key to match save
                                timeAgo = doc.getString("timeAgo") ?: "Hace un momento",
                                tag = doc.getString("tag") ?: "General",
                                likesCount = doc.getLong("likesCount")?.toInt() ?: 0,
                                upVotes = doc.getLong("upVotes")?.toInt() ?: 0,
                                downVotes = doc.getLong("downVotes")?.toInt() ?: 0,
                                commentsCount = doc.getLong("commentsCount")?.toInt() ?: 0,
                                score = doc.getLong("score")?.toInt() ?: 0,
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

    fun votePost(
        postId: String,
        userId: String,
        voteType: String // "up" or "down"
    ) {
        db.runTransaction { transaction ->
            val postRef = db.collection("posts").document(postId)
            val voteRef = postRef.collection("votes").document(userId)

            val postSnapshot = transaction.get(postRef)
            val voteSnapshot = transaction.get(voteRef)

            val currentUpVotes = postSnapshot.getLong("upVotes") ?: 0
            val currentDownVotes = postSnapshot.getLong("downVotes") ?: 0

            var newUpVotes = currentUpVotes
            var newDownVotes = currentDownVotes

            if (voteSnapshot.exists()) {
                val currentVoteType = voteSnapshot.getString("type")
                if (currentVoteType == voteType) {
                    // Remove vote
                    transaction.delete(voteRef)
                    if (voteType == "up") newUpVotes-- else newDownVotes--
                } else {
                    // Switch vote
                    transaction.update(voteRef, "type", voteType)
                    if (voteType == "up") {
                        newUpVotes++
                        newDownVotes--
                    } else {
                        newUpVotes--
                        newDownVotes++
                    }
                }
            } else {
                // New vote
                val voteData = hashMapOf(
                    "type" to voteType,
                    "timestamp" to Timestamp.now()
                )
                transaction.set(voteRef, voteData)
                if (voteType == "up") newUpVotes++ else newDownVotes++
            }

            transaction.update(postRef, "upVotes", newUpVotes)
            transaction.update(postRef, "downVotes", newDownVotes)
            transaction.update(postRef, "score", newUpVotes - newDownVotes)
        }.addOnFailureListener { e ->
            _error.value = "Error al votar: ${e.message}"
        }
    }

    fun createPostWithImage(
        title: String,
        body: String,
        authorName: String,
        userId: String,
        authorProfileUrl: String,
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
                        savePostToFirestore(title, body, authorName, userId, authorProfileUrl, tag, downloadUrl.toString(), onSuccess, onError)
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
            savePostToFirestore(title, body, authorName, userId, authorProfileUrl, tag, null, onSuccess, onError)
        }
    }

    private fun savePostToFirestore(
        title: String,
        body: String,
        authorName: String,
        userId: String,
        authorProfileUrl: String,
        tag: String,
        imageUrl: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val post = hashMapOf(
            "userId" to userId,
            "title" to title,
            "body" to body,
            "authorName" to authorName,
            "authorProfileUrl" to authorProfileUrl,
            "tag" to tag,
            "timeAgo" to "Hace un momento", // In a real app, calculate this on read or store timestamp
            "likesCount" to 0,
            "upVotes" to 0,
            "downVotes" to 0,
            "commentsCount" to 0,
            "commentsCount" to 0,
            "score" to 0,
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