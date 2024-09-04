package com.msaggik.playlistmaker.media.presentation.view_model.state

import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks

interface CreatePlaylistWithTracksState {
    data class Content(
        val playlists: List<PlaylistWithTracks>
    ) : CreatePlaylistWithTracksState

    object  Empty : CreatePlaylistWithTracksState
}