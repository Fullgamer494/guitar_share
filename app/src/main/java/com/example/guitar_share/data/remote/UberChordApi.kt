package com.example.guitar_share.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface UberChordApi {
    @GET("chords/{chord}")
    suspend fun getChord(@Path("chord") chord: String): List<ChordResponse>
}

data class ChordResponse(
    val strings: String,
    val fingering: String,
    val chordName: String,
    val enharmonicChordName: String,
    val voicingID: String,
    val tones: String
)
