package com.msaggik.playlistmaker.media.domain.models

data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
)
