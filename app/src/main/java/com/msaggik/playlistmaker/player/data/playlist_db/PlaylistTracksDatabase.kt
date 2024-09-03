package com.msaggik.playlistmaker.player.data.playlist_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.msaggik.playlistmaker.player.data.playlist_db.dao.PlaylistTracksDao
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig.DATABASE_NAME
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistAndTrackEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

@Database(version = 2, entities = [PlaylistEntity::class, TrackEntity::class, PlaylistAndTrackEntity::class])
abstract class PlaylistTracksDatabase : RoomDatabase() {
    abstract fun playlistTracksDao() : PlaylistTracksDao

    companion object {
        @Volatile
        private var INSTANCE: PlaylistTracksDatabase? = null

        fun getDatabase(context: Context, migration: Migration): PlaylistTracksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaylistTracksDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(migration)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}