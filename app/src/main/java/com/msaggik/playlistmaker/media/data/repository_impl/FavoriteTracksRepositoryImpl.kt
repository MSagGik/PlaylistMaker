package com.msaggik.playlistmaker.media.data.repository_impl

import com.msaggik.playlistmaker.media.data.converters.TrackDbConverter
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.TracksDatabase
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val tracksDataBase: TracksDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteTracksRepository {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return tracksDataBase.favoriteTracksDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteFavoriteTrack(track: Track): Int {
        return tracksDataBase.favoriteTracksDao().deleteTrack(trackDbConverter.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        emit(
            tracksDataBase
                .favoriteTracksDao()
                .getFavoriteTracks()
                .map { trackEntity -> trackDbConverter.map(trackEntity)}
        )
    }

    override fun getFavoriteTracksId(): Flow<List<Long>> = flow {
        emit(
            tracksDataBase
                .favoriteTracksDao()
                .getFavoriteTracksIds()
        )
    }

}