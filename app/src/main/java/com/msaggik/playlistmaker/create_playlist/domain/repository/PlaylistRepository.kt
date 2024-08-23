package com.msaggik.playlistmaker.create_playlist.domain.repository

import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist) : Long

    fun namesPlaylist(): Flow<List<String>>
}