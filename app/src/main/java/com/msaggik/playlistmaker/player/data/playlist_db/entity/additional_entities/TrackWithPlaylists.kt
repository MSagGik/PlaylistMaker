package com.msaggik.playlistmaker.player.data.playlist_db.entity.additional_entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistAndTrackEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

data class TrackWithPlaylists(
    @Embedded
    val trackEntity: TrackEntity,
    @Relation(
        parentColumn = DatabaseConfig.TRACK_ID,
        entityColumn = DatabaseConfig.PLAYLIST_ID,
        entity = PlaylistEntity::class,
        associateBy = Junction(PlaylistAndTrackEntity::class, parentColumn = DatabaseConfig.ID_TRACK, entityColumn = DatabaseConfig.ID_PLAYLIST)
    )
    val playlists: List<PlaylistEntity>
)