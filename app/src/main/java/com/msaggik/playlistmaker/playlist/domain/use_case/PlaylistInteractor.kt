package com.msaggik.playlistmaker.playlist.domain.use_case

import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

    suspend fun removeTrackFromPlaylist(idPlaylist: Long, idTrack: Long): Int

    suspend fun sharePlaylist(infoPlaylist: String)

    suspend fun removePlaylist(playlistId: Long)
}