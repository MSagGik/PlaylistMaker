package com.msaggik.playlistmaker.create_playlist.domain.models

data class Playlist (
    val playlistId: Long = 0L,
    val playlistName: String = "",
    val playlistDescription: String = "",
    val playlistUriAlbum: String = ""
)