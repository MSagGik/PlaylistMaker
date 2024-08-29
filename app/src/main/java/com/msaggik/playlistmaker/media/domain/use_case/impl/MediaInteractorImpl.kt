package com.msaggik.playlistmaker.media.domain.use_case.impl

import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl (
    private val repository: MediaRepository
) : MediaInteractor {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return repository.addFavoriteTrack(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }

    override fun playlistsWithTracks(): Flow<List<PlaylistWithTracks>> {
        return repository.playlistsWithTracks()
    }
}