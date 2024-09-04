package com.msaggik.playlistmaker.playlist_manager.presentation.ui.state

import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track

sealed class PlaylistManagerState {
    data class TrackArg(val track: Track) : PlaylistManagerState()
    data class EditPlaylistArg(val playlist: Playlist) : PlaylistManagerState()
    object EmptyArg : PlaylistManagerState()
}