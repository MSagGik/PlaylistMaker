package com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig

@Entity(tableName = DatabaseConfig.TRACK_TABLE)
data class TrackEntity (
    @PrimaryKey
    @ColumnInfo(name = DatabaseConfig.TRACK_ID)
    val trackId: Long,
    @ColumnInfo(name = DatabaseConfig.TRACK_NAME)
    val trackName: String = "",
    @ColumnInfo(name = DatabaseConfig.ARTIST_NAME)
    val artistName: String = "",
    @ColumnInfo(name = DatabaseConfig.TRACK_TIME_MILLIS)
    val trackTimeMillis: Long = -1L,
    @ColumnInfo(name = DatabaseConfig.ARTWORK_URL)
    val artworkUrl100: String = "",
    @ColumnInfo(name = DatabaseConfig.COLLECTION_NAME)
    val collectionName: String = "",
    @ColumnInfo(name = DatabaseConfig.RELEASE_DATE)
    val releaseDate: String = "",
    @ColumnInfo(name = DatabaseConfig.PRIMARY_GENRE_NAME)
    val primaryGenreName: String = "",
    val country: String = "",
    @ColumnInfo(name = DatabaseConfig.PREVIEW_URL)
    val previewUrl: String = "",
    @ColumnInfo(name = DatabaseConfig.DATE_ADD_TRACK)
    val dateAddTrack: Long = -1L,
    @ColumnInfo(name = DatabaseConfig.FAVORITE_TRACK)
    val favoriteTrack: Boolean = false
)