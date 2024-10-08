package com.msaggik.playlistmaker.playlist_manager.data.repository_impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.msaggik.playlistmaker.playlist_manager.data.mappers.PlaylistManagerMapper
import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track
import com.msaggik.playlistmaker.playlist_manager.domain.repository.PlaylistManagerRepository
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import com.msaggik.playlistmaker.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistManagerRepositoryImpl(
    private val context: Context,
    private val dataBase: PlaylistTracksDatabase
) : PlaylistManagerRepository {

    override suspend fun addPlaylist(playlist: Playlist): Long {
        return dataBase.playlistTracksDao().insertPlaylist(PlaylistManagerMapper.map(playlist))
    }

    override suspend fun insertPlaylistAndAddTrackInPlaylist(
        playlist: Playlist,
        track: Track
    ): Long {
        return  dataBase.playlistTracksDao()
            .insertPlaylistAndAddTrackInPlaylist(PlaylistManagerMapper.map(playlist), PlaylistManagerMapper.map(track))
    }

    override fun namesPlaylist(): Flow<List<String>> = flow {
        emit(
            dataBase.playlistTracksDao().namesPlaylist()
        )
    }

    override suspend fun insertPlaylist(playlist: Playlist): Long {
        return dataBase.playlistTracksDao().insertPlaylist(PlaylistManagerMapper.map(playlist))
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): Uri {
        // read image
        val inputStream = context.contentResolver.openInputStream(uri)
        // create file
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_covers"
        )
        if (!filePath.exists()) filePath.mkdirs() // read/create directory
        val fileName = Utils.dateFormatNameAlbumPlaylist(System.currentTimeMillis())
        val file = File(filePath, fileName)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        val privateUri = file.toUri()
        // save image
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return privateUri
    }
}