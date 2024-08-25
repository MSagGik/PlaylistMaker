package com.msaggik.playlistmaker.player.domain.models

data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
)
