package com.msaggik.playlistmaker.media.data.mappers

import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

class FavoriteTrackMapper {

    fun map(trackEntity: TrackEntity): Track {
        return with(trackEntity) {
            Track(
                trackId = trackId.toInt(),
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                dateAddTrack = dateAddTrack
            )
        }
    }

    fun map(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                trackId = trackId.toLong(),
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                dateAddTrack = dateAddTrack
            )
        }
    }
}