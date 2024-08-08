package com.msaggik.data.api.db.favorite_tracks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msaggik.data.api.db.favorite_tracks.entity.config_db.FavoriteTracksConfig

@Entity(tableName = FavoriteTracksConfig.FAVORITE_TRACKS_TABLE)
data class TrackEntity(
    @PrimaryKey
    @ColumnInfo(name = FavoriteTracksConfig.TRACK_ID)
    val trackId: Long,
    @ColumnInfo(name = FavoriteTracksConfig.TRACK_NAME)
    val trackName: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.ARTIST_NAME)
    val artistName: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.TRACK_TIME_MILLIS)
    val trackTimeMillis: Long = -1L,
    @ColumnInfo(name = FavoriteTracksConfig.ARTWORK_URL)
    val artworkUrl100: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.COLLECTION_NAME)
    val collectionName: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.RELEASE_DATE)
    val releaseDate: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.PRIMARY_GENRE_NAME)
    val primaryGenreName: String = "",
    val country: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.PREVIEW_URL)
    val previewUrl: String = "",
    @ColumnInfo(name = FavoriteTracksConfig.DATE_ADD_TRACK)
    val dateAddTrack: Long = -1L
)
