package com.msaggik.playlistmaker.playlist.presentation.view_model.state

import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks

sealed interface PlaylistWithTracksState {

    data class Content(
        val playlist: PlaylistWithTracks
    ) : PlaylistWithTracksState

    object Empty : PlaylistWithTracksState
}