package com.example.guitar_share.presentation.ui.screens.forum

data class ForumPostData(
    val id: Int,
    val title: String,
    val body: String,
    val authorName: String,
    val profilePictureUrl: String? = null,
    val timeAgo: String,
    val tag: String,
    val likesCount: Int,
    val commentsCount: Int,
    val imageUrl: String? = null
)