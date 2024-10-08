package com.msaggik.playlistmaker.player.presentation.view_model.state

import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks

interface PlaylistsWithTracksState {
    data class Content(
        val playlists: List<PlaylistWithTracks>
    ) : PlaylistsWithTracksState

    object  Empty : PlaylistsWithTracksState
}