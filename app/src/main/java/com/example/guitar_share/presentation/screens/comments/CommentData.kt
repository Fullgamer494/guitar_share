package com.example.guitar_share.presentation.screens.comments

import com.google.firebase.Timestamp

data class CommentData(
    val id: String = "",
    val postId: String = "",
    val userId: String = "",
    val authorName: String = "",
    val authorProfileUrl: String? = null,
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Timestamp? = null,
    val parentId: String? = null // For nesting: null = root comment, string = reply to commentId
)
