package com.msaggik.playlistmaker.media.presentation.view_model.state

import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks

interface PlaylistWithTracksState {
    data class Content(
        val playlists: List<PlaylistWithTracks>
    ) : PlaylistWithTracksState

    object  Empty : PlaylistWithTracksState
}