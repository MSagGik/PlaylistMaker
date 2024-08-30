package com.msaggik.playlistmaker.playlist.domain.use_case

import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>
}