package com.msaggik.playlistmaker.player.data.playlist_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msaggik.playlistmaker.player.data.playlist_db.dao.PlaylistTracksDao
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistAndTrackEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

@Database(version = 1, entities = [PlaylistEntity::class, TrackEntity::class, PlaylistAndTrackEntity::class])
abstract class PlaylistTracksDatabase : RoomDatabase() {
    abstract fun playlistTracksDao() : PlaylistTracksDao
}