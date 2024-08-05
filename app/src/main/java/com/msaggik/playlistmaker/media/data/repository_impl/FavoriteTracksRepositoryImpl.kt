package com.msaggik.playlistmaker.media.data.repository_impl

import com.msaggik.playlistmaker.media.data.converters.TrackDbConverter
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.TracksDatabase
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val tracksDataBase: TracksDatabase,
) : FavoriteTracksRepository {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return tracksDataBase.favoriteTracksDao().insertTrack(TrackDbConverter.map(track))
    }

    override suspend fun deleteFavoriteTrack(idTrack: Long): Boolean {
        return tracksDataBase.favoriteTracksDao().deleteTrack(idTrack)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        tracksDataBase.favoriteTracksDao().getFavoriteTracks().map { trackEntity -> TrackDbConverter.map(trackEntity) }
    }

}