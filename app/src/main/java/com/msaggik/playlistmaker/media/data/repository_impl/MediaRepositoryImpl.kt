package com.msaggik.playlistmaker.media.data.repository_impl

import com.msaggik.playlistmaker.media.data.mappers.MediaMapper
import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.repository.MediaRepository
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaRepositoryImpl(
    private val dataBase: PlaylistTracksDatabase,
    private val mediaMapper: MediaMapper
) : MediaRepository {

    override suspend fun addFavoriteTrack(track: Track): Long {
        return dataBase.playlistTracksDao()
            .insertOrUpdateTrack(mediaMapper.mapTrackToTrackEntity(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        emit(
            dataBase.playlistTracksDao()
                .getFavoriteTracks()
                .map { trackEntity -> mediaMapper.mapTrackEntityToTrack(trackEntity) }
        )
    }

    override fun playlistsWithTracks(): Flow<List<PlaylistWithTracks>> = flow {
        emit(
            dataBase.playlistTracksDao().playlistsWithTracks().map { playlist ->
                mediaMapper.mapPlaylistDbToPlaylist(playlist)
            }
        )
    }

}