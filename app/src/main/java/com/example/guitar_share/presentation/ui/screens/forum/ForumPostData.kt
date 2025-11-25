package com.example.guitar_share.presentation.ui.screens.forum

data class ForumPostData(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val authorName: String = "",
    val profilePictureUrl: String? = null,
    val timeAgo: String = "",
    val tag: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val imageUrl: String? = null
)