package com.msaggik.playlistmaker.playlist_manager.domain.use_case

import android.net.Uri
import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistManagerInteractor {

    suspend fun addPlaylist(playlist: Playlist) : Long
    suspend fun insertPlaylistAndAddTrackInPlaylist(playlist: Playlist, track: Track): Long

    fun namesPlaylist(): Flow<List<String>>

    suspend fun insertPlaylist(playlist: Playlist): Long

    suspend fun saveImageToPrivateStorage(uri: Uri): Uri
}