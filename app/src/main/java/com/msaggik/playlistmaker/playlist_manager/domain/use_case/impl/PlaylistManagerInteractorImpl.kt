package com.msaggik.playlistmaker.playlist_manager.domain.use_case.impl

import android.net.Uri
import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track
import com.msaggik.playlistmaker.playlist_manager.domain.repository.PlaylistManagerRepository
import com.msaggik.playlistmaker.playlist_manager.domain.use_case.PlaylistManagerInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistManagerInteractorImpl(
    private val repository: PlaylistManagerRepository
) : PlaylistManagerInteractor {

    override suspend fun addPlaylist(playlist: Playlist): Long {
        return repository.addPlaylist(playlist)
    }

    override fun namesPlaylist(): Flow<List<String>> {
        return repository.namesPlaylist()
    }

    override suspend fun insertPlaylist(playlist: Playlist): Long {
        return repository.insertPlaylist(playlist)
    }

    override suspend fun insertPlaylistAndAddTrackInPlaylist(
        playlist: Playlist,
        track: Track
    ): Long {
       return repository.insertPlaylistAndAddTrackInPlaylist(playlist, track)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): Uri {
        return repository.saveImageToPrivateStorage(uri)
    }
}