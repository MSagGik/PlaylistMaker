package com.msaggik.playlistmaker.media.domain.use_case.impl

import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl (
    private val repository: FavoriteTracksRepository
) : MediaInteractor {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return repository.addFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: Track): Int {
        return repository.deleteFavoriteTrack(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }

    override fun getFavoriteTracksId(): Flow<List<Long>> {
        return repository.getFavoriteTracksId()
    }

}