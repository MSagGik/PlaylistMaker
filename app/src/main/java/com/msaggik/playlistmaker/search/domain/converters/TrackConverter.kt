package com.msaggik.playlistmaker.search.domain.converters

import com.msaggik.playlistmaker.search.domain.models.Track

object TrackConverter {
    fun map(track: Track): com.msaggik.playlistmaker.media.domain.models.Track {
        return with(track) {
            com.msaggik.playlistmaker.media.domain.models.Track(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                isFavorite = isFavorite,
                dateAddTrack = dateAddTrack
            )
        }
    }

    fun map(track: com.msaggik.playlistmaker.media.domain.models.Track): Track {
        return with(track) {
            Track(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                isFavorite = isFavorite,
                dateAddTrack = dateAddTrack
            )
        }
    }
}