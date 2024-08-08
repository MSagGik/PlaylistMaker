package com.msaggik.data.api.network.dto.response.entities

import java.io.Serializable

data class TrackDto (
    val trackId: Int = -1,
    val trackName: String = "",
    val artistName: String = "",
    val trackTimeMillis: Long = -1L,
    val artworkUrl100: String = "",
    val collectionName: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String = "",
    val country: String = "",
    val previewUrl: String = ""
) : Serializable
