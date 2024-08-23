package com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig

@Entity(tableName = DatabaseConfig.PLAYLIST_AND_TRACK_TABLE, primaryKeys = [DatabaseConfig.ID_PLAYLIST, DatabaseConfig.ID_TRACK])
data class PlaylistAndTrackEntity(
    @ColumnInfo(name = DatabaseConfig.ID_PLAYLIST)
    val idPlaylist: Long,
    @ColumnInfo(name = DatabaseConfig.ID_TRACK)
    val idTrack: Long
)
