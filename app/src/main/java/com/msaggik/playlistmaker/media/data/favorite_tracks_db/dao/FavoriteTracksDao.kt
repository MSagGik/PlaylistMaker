package com.msaggik.playlistmaker.media.data.favorite_tracks_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.entity.TrackEntity

@Dao
interface FavoriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity) : Long

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity) : Int

    @Query("SELECT * FROM FAVORITE_TRACKS_TABLE ORDER BY dateAddTrack DESC")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT TRACK_ID FROM FAVORITE_TRACKS_TABLE")
    suspend fun getFavoriteTracksIds(): List<Long>
}