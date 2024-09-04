package com.msaggik.playlistmaker.playlist.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
) : Parcelable
