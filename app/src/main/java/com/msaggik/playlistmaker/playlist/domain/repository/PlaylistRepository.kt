package com.msaggik.playlistmaker.playlist.domain.repository

import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

    suspend fun removeTrackFromPlaylist(idPlaylist: Long, idTrack: Long): Int

    suspend fun sharePlaylist(infoPlaylist: String)

    suspend fun removePlaylist(playlistId: Long)
}