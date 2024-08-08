package com.msaggik.data.repository_impl

import com.msaggik.data.api.db.favorite_tracks.TracksDatabase
import com.msaggik.data.api.network.mappers.FavoriteTrackMapper
import com.msaggik.media.domain.models.Track
import com.msaggik.media.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val tracksDataBase: TracksDatabase,
    private val favoriteTrackMapper: FavoriteTrackMapper
) : FavoriteTracksRepository {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return tracksDataBase.favoriteTracksDao().insertTrack(favoriteTrackMapper.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        emit(
            tracksDataBase
                .favoriteTracksDao()
                .getFavoriteTracks()
                .map { trackEntity -> favoriteTrackMapper.map(trackEntity)}
        )
    }

}