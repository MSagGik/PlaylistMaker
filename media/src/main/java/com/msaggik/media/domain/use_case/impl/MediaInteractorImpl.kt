package com.msaggik.media.domain.use_case.impl

import com.msaggik.media.domain.models.Track
import com.msaggik.media.domain.use_case.MediaInteractor
import com.msaggik.media.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl (
    private val repository: FavoriteTracksRepository
) : MediaInteractor {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return repository.addFavoriteTrack(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }

}