package com.msaggik.playlistmaker.media.domain.models

data class Playlist(
    val playlistId: Long = 0L,
    val playlistName: String = "",
    val playlistDescription: String = "",
    val playlistUriAlbum: String = ""
)
