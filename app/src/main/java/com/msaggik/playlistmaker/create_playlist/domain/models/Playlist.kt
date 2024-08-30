package com.msaggik.playlistmaker.create_playlist.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist (
    var playlistId: Long = 0L,
    val playlistName: String = "",
    val playlistDescription: String = "",
    val playlistUriAlbum: String = ""
) : Parcelable