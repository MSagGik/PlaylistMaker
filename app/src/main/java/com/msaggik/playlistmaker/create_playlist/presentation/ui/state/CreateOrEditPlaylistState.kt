package com.msaggik.playlistmaker.create_playlist.presentation.ui.state

import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track

sealed class CreateOrEditPlaylistState {
    data class TrackArg(val track: Track) : CreateOrEditPlaylistState()
    data class EditPlaylistArg(val playlist: Playlist) : CreateOrEditPlaylistState()
    object EmptyArg : CreateOrEditPlaylistState()
}