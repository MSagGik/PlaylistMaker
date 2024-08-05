package com.msaggik.playlistmaker.media.data.converters

import com.msaggik.playlistmaker.media.data.favorite_tracks_db.entity.TrackEntity
import com.msaggik.playlistmaker.media.domain.models.Track

object TrackDbConverter {

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
                previewUrl = previewUrl
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
                previewUrl = previewUrl
            )
        }
    }
}