package com.msaggik.playlistmaker.playlist.domain.repository

import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>
}