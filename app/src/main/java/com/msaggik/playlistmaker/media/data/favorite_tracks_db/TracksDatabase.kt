package com.msaggik.playlistmaker.media.data.favorite_tracks_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.dao.FavoriteTracksDao
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class TracksDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
}