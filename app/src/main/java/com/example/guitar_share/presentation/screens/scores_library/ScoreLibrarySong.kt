package com.example.guitar_share.presentation.screens.scores_library

data class ScoreLibrarySong(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val artist: String = "",
    val genre: String = "",
    val key: String = "",
    val coverUrl: String = "",
    val tags: List<String> = emptyList()
)
