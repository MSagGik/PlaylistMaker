package com.msaggik.playlistmaker.playlist.data.repository_impl

import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import com.msaggik.playlistmaker.playlist.data.mappers.PlaylistMapper
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val dataBase: PlaylistTracksDatabase,
    private val playlistMapper: PlaylistMapper
) : PlaylistRepository {

    override suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> = flow {
        emit(
            playlistMapper.mapPlaylistDbToPlaylist(dataBase.playlistTracksDao().playlistWithTracks(playlistId))
        )
    }
}