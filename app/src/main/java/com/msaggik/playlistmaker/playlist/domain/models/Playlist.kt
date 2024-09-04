package com.msaggik.playlistmaker.playlist.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Long = 0L,
    val playlistName: String = "",
    val playlistDescription: String = "",
    val playlistUriAlbum: String = ""
) : Parcelable
