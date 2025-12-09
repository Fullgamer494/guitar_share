package com.example.guitar_share.presentation.screens.forum

data class ForumPostData(
    val id: String = "",
    val userId: String = "", // Added userId
    val title: String = "",
    val body: String = "",
    val authorName: String = "",
    val profilePictureUrl: String? = null,
    val timeAgo: String = "",
    val tag: String = "",
    val likesCount: Int = 0, // Keeping for backward compatibility or general likes
    val upVotes: Int = 0,    // Added upVotes
    val downVotes: Int = 0,  // Added downVotes
    val score: Int = 0,      // Added for sorting (upVotes - downVotes)
    val commentsCount: Int = 0,
    val imageUrl: String? = null
)