package com.msaggik.playlistmaker.create_playlist.data.playlist_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.msaggik.playlistmaker.create_playlist.data.playlist_db.entity.additional_entities.PlaylistWithTracks
import com.msaggik.playlistmaker.create_playlist.data.playlist_db.entity.additional_entities.TrackWithPlaylists
import com.msaggik.playlistmaker.create_playlist.data.playlist_db.entity.config_db.DatabaseConfig
import com.msaggik.playlistmaker.create_playlist.data.playlist_db.entity.many_to_many.PlaylistAndTrackEntity
import com.msaggik.playlistmaker.create_playlist.data.playlist_db.entity.many_to_many.TrackEntity

@Dao
interface PlaylistTracksDao {

    // PLAYLISTS AND TRACKS
    // delete playlist
    @Transaction
    suspend fun removePlaylist(playlistId: Long) {
        val tracksForPlaylist = getTracksForPlaylist(playlistId)
        deletePlaylistInTrackAndPlaylist(playlistId)
        deletePlaylistInPlaylists(playlistId)
        tracksForPlaylist.forEach { track ->
            val hasOtherPlaylists = checkIfTrackHasOtherPlaylists(track.trackId)
            if (!hasOtherPlaylists && !track.favoriteTrack) {
                deleteTrack(track)
            }
        }
    }

    @Transaction
    @Query("SELECT t.* FROM ${DatabaseConfig.TRACK_TABLE} t JOIN ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} pt ON t.track_id = pt.id_track WHERE pt.id_playlist = :playlistId")
    suspend fun getTracksForPlaylist(playlistId: Long): List<TrackEntity>

    @Query("DELETE FROM ${DatabaseConfig.PLAYLIST_TABLE} WHERE ${DatabaseConfig.PLAYLIST_ID} = :playlistId")
    suspend fun deletePlaylistInPlaylists(playlistId: Long)

    @Query("DELETE FROM ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} WHERE ${DatabaseConfig.ID_PLAYLIST} = :playlistId")
    suspend fun deletePlaylistInTrackAndPlaylist(playlistId: Long)

    @Query("SELECT COUNT(*) > 0 FROM ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} WHERE ${DatabaseConfig.ID_TRACK} = :trackId")
    suspend fun checkIfTrackHasOtherPlaylists(trackId: Long): Boolean

    // delete track in playlist
    @Transaction
    suspend fun removeTrackFromPlaylist(idPlaylist: Long, idTrack: Long): Int {
        val responseDatabase = removeItemPlaylistTrack(idPlaylist, idTrack)
        return if (responseDatabase != -1) {
            val existingEntry = isTrackInPlaylistAndTrack(idTrack)
            if(existingEntry) {
                responseDatabase
            } else {
                val track = getTrackInTracks(idTrack)
                if(!track.favoriteTrack) deleteTrack(track) else responseDatabase
            }
        } else {
            responseDatabase
        }
    }

    @Query("SELECT * FROM ${DatabaseConfig.TRACK_TABLE} WHERE ${DatabaseConfig.TRACK_ID} = :trackId")
    suspend fun getTrackInTracks(trackId: Long): TrackEntity

    @Query("SELECT COUNT(*) > 0 FROM ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} WHERE ${DatabaseConfig.ID_TRACK} = :idTrack")
    suspend fun isTrackInPlaylistAndTrack(idTrack: Long): Boolean

    @Query("DELETE FROM ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} WHERE ${DatabaseConfig.ID_PLAYLIST} = :idPlaylist AND ${DatabaseConfig.ID_TRACK} = :idTrack")
    suspend fun removeItemPlaylistTrack(idPlaylist: Long, idTrack: Long): Int

    // add track in playlist
    @Transaction
    suspend fun addTrackInPlaylist(idPlaylist: Long, track: TrackEntity): Long {
        val existingEntry = checkIfExistsPlaylistAndTrack(idPlaylist, track.trackId)
        return if (existingEntry) {
            -1
        } else {
            if(!checkIfExistsTracks(track.trackId)) insertOrUpdateTrack(track = track)
            insertPlaylistAndTrack(listOf(PlaylistAndTrackEntity(idPlaylist, track.trackId))).first() // return idTrack
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistAndTrack(playlistAndTracksEntities: List<PlaylistAndTrackEntity>): List<Long>

    @Query("SELECT COUNT(*) > 0 FROM ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} WHERE ${DatabaseConfig.ID_PLAYLIST} = :idPlaylist AND ${DatabaseConfig.ID_TRACK} = :idTrack")
    suspend fun checkIfExistsPlaylistAndTrack(idPlaylist: Long, idTrack: Long): Boolean

    @Query("SELECT COUNT(*) > 0 FROM ${DatabaseConfig.TRACK_TABLE} WHERE ${DatabaseConfig.TRACK_ID} = :trackId")
    suspend fun checkIfExistsTracks(trackId: Long): Boolean

    // read playlists and tracks
    @Transaction
    @Query("SELECT * FROM ${DatabaseConfig.PLAYLIST_TABLE}")
    suspend fun playlistWithTracks(): List<PlaylistWithTracks>

    @Transaction
    @Query("SELECT * FROM ${DatabaseConfig.TRACK_TABLE}")
    suspend fun trackWithPlaylists(): List<TrackWithPlaylists>

    // FAVORITE TRACKS
    // Query to get a list of track IDs of all posts with `favoriteTrack` equal to true
    @Query("SELECT ${DatabaseConfig.TRACK_ID} FROM ${DatabaseConfig.TRACK_TABLE}")
    suspend fun getFavoriteTracksIds(): List<Long>

    // Query to get all posts with `favoriteTrack` equal to true
    @Query("SELECT * FROM ${DatabaseConfig.TRACK_TABLE} WHERE ${DatabaseConfig.FAVORITE_TRACK} = 1 ORDER BY ${DatabaseConfig.DATE_ADD_TRACK} DESC")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    // Query to check if a record exists and change the `favoriteTrack` field
    @Transaction
    suspend fun setFavoriteTrack(track: TrackEntity) : Long {
        return if(track.favoriteTrack) {
            insertOrUpdateTrack(track)
        } else {
            val listCheck = getPlaylistAndTrackEntitiesById(track.trackId)
            if (listCheck.isEmpty()) {
                deleteTrack(track).toLong()
            } else {
                insertOrUpdateTrack(track)
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTrack(track: TrackEntity) : Long

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity) : Int

    @Query("SELECT * FROM ${DatabaseConfig.PLAYLIST_AND_TRACK_TABLE} WHERE ${DatabaseConfig.ID_TRACK} = :trackId")
    suspend fun getPlaylistAndTrackEntitiesById(trackId: Long): List<PlaylistAndTrackEntity>
}