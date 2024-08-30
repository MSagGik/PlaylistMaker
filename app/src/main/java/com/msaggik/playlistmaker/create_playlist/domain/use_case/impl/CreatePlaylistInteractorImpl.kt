package com.msaggik.playlistmaker.create_playlist.domain.use_case.impl

import android.net.Uri
import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import com.msaggik.playlistmaker.create_playlist.domain.repository.CreatePlaylistRepository
import com.msaggik.playlistmaker.create_playlist.domain.use_case.CreatePlaylistInteractor
import kotlinx.coroutines.flow.Flow

class CreatePlaylistInteractorImpl(
    private val repository: CreatePlaylistRepository
) : CreatePlaylistInteractor {

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