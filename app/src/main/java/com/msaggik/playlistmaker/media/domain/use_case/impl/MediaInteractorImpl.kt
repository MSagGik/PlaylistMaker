package com.msaggik.playlistmaker.media.domain.use_case.impl

import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class MediaInteractorImpl (
    private val repository: FavoriteTracksRepository
) : MediaInteractor {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return repository.addFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(idTrack: Long): Boolean {
        return repository.deleteFavoriteTrack(idTrack)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }
}