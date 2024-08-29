package com.msaggik.playlistmaker.player.data.playlist_db.entity.additional_entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistAndTrackEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

data class PlaylistWithTracks (
    @Embedded
    val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = DatabaseConfig.PLAYLIST_ID,
        entityColumn = DatabaseConfig.TRACK_ID,
        entity = TrackEntity::class,
        associateBy = Junction(PlaylistAndTrackEntity::class, parentColumn = DatabaseConfig.ID_PLAYLIST, entityColumn = DatabaseConfig.ID_TRACK)
    )
    val tracks: List<TrackEntity>
)