package com.msaggik.playlistmaker.player.presentation.view_model.state

import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks

interface PlaylistWithTracksState {
    data class Content(
        val playlists: List<PlaylistWithTracks>
    ) : PlaylistWithTracksState

    object  Empty : PlaylistWithTracksState
}