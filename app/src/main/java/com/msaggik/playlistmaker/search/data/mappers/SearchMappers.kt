package com.msaggik.playlistmaker.search.data.mappers

import com.msaggik.playlistmaker.search.domain.models.Track

object SearchMappers {

    fun mapSearchToPlayer(track: Track): com.msaggik.playlistmaker.player.domain.models.Track {
        return with(track) {
            com.msaggik.playlistmaker.player.domain.models.Track(
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