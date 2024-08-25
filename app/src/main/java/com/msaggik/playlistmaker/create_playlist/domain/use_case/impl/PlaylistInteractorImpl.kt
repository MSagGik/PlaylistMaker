package com.msaggik.playlistmaker.create_playlist.domain.use_case.impl

import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import com.msaggik.playlistmaker.create_playlist.domain.repository.PlaylistRepository
import com.msaggik.playlistmaker.create_playlist.domain.use_case.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist): Long {
        return repository.addPlaylist(playlist)
    }

    override fun namesPlaylist(): Flow<List<String>> {
        return repository.namesPlaylist()
    }

    override suspend fun insertPlaylistAndAddTrackInPlaylist(
        playlist: Playlist,
        track: Track
    ): Long {
       return repository.insertPlaylistAndAddTrackInPlaylist(playlist, track)
    }
}