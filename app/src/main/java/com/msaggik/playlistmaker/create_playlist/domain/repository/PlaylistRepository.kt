package com.msaggik.playlistmaker.create_playlist.domain.repository

import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist) : Long
    suspend fun insertPlaylistAndAddTrackInPlaylist(playlist: Playlist, track: Track): Long

    fun namesPlaylist(): Flow<List<String>>
}