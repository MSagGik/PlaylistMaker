package com.msaggik.playlistmaker.media.data.repository_impl

import com.msaggik.playlistmaker.media.data.mappers.FavoriteTrackMapper
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val dataBase: PlaylistTracksDatabase,
    private val favoriteTrackMapper: FavoriteTrackMapper
) : FavoriteTracksRepository {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return dataBase.playlistTracksDao().insertOrUpdateTrack(favoriteTrackMapper.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        emit(
            dataBase.playlistTracksDao()
                .getFavoriteTracks()
                .map { trackEntity -> favoriteTrackMapper.map(trackEntity)}
        )
    }

}