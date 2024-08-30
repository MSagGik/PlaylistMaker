package com.msaggik.playlistmaker.playlist.data.repository_impl

import android.content.Context
import android.content.Intent
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import com.msaggik.playlistmaker.playlist.data.mappers.PlaylistMapper
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val context: Context,
    private val dataBase: PlaylistTracksDatabase,
    private val playlistMapper: PlaylistMapper
) : PlaylistRepository {

    override suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> = flow {
        emit(
            playlistMapper.mapPlaylistDbToPlaylist(dataBase.playlistTracksDao().playlistWithTracks(playlistId))
        )
    }

    override suspend fun removeTrackFromPlaylist(idPlaylist: Long, idTrack: Long): Int {
        return dataBase.playlistTracksDao().removeTrackFromPlaylist(idPlaylist, idTrack)
    }

    override suspend fun sharePlaylist(infoPlaylist: String) {
        val formShareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, infoPlaylist)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(formShareIntent, context.getString(R.string.default_user))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}