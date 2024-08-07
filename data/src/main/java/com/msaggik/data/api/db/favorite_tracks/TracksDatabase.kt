package com.msaggik.data.api.db.favorite_tracks

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msaggik.data.api.db.favorite_tracks.dao.FavoriteTracksDao
import com.msaggik.data.api.db.favorite_tracks.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class TracksDatabase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
}