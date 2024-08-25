package com.msaggik.playlistmaker.create_playlist.data.repository_impl

import com.msaggik.playlistmaker.create_playlist.data.mappers.PlaylistMapper
import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import com.msaggik.playlistmaker.create_playlist.domain.repository.PlaylistRepository
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val dataBase: PlaylistTracksDatabase,
    private val playlistMapper: PlaylistMapper
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist): Long {
        return dataBase.playlistTracksDao().insertPlaylist(playlistMapper.map(playlist))
    }

    override suspend fun insertPlaylistAndAddTrackInPlaylist(
        playlist: Playlist,
        track: Track
    ): Long {
        return  dataBase.playlistTracksDao()
            .insertPlaylistAndAddTrackInPlaylist(playlistMapper.map(playlist), playlistMapper.map(track))
    }

    override fun namesPlaylist(): Flow<List<String>> = flow {
        emit(
            dataBase.playlistTracksDao().namesPlaylist()
        )
    }
}