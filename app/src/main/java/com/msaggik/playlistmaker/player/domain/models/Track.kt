package com.msaggik.playlistmaker.player.domain.models

import java.io.Serializable

data class Track (
    val trackId: Int = -1,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false,
    var dateAddTrack: Long = -1L
) : Serializable
