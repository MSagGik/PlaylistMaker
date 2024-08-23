package com.msaggik.playlistmaker.create_playlist.domain.use_case

import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist) : Long

    fun namesPlaylist(): Flow<List<String>>
}