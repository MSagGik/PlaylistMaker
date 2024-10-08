package com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig

@Entity(tableName = DatabaseConfig.PLAYLIST_TABLE)
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseConfig.PLAYLIST_ID)
    val playlistId: Long = 0L,
    @ColumnInfo(name = DatabaseConfig.PLAYLIST_NAME)
    val playlistName: String = "",
    @ColumnInfo(name = DatabaseConfig.PLAYLIST_DESCRIPTION)
    val playlistDescription: String = "",
    @ColumnInfo(name = DatabaseConfig.PLAYLIST_URI_ALBUM)
    val playlistUriAlbum: String = ""
)