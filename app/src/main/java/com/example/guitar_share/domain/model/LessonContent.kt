package com.example.guitar_share.domain.model

data class LessonContent(
    val title: String,
    val subtitle: String,
    val imageUrl: Any, // Can be Int (Resource) or String (URL)
    val body: String,
    // Dynamic Chord Data
    val chordName: String? = null, 
    val fingering: String? = null,
    val strings: String? = null
)
