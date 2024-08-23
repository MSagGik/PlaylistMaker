package com.msaggik.playlistmaker.player.data.mappers

import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.search.data.dto.response.TrackDto

class PlayerMapper {
    fun convertTrackDtoToTrack(track: TrackDto): Track {
        return Track(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
    }
    fun convertTrackToTrackDto(track: Track): TrackDto {
        return TrackDto(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
    }
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
                dateAddTrack = dateAddTrack,
                isFavorite = favoriteTrack
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
                favoriteTrack = isFavorite,
                dateAddTrack = dateAddTrack
            )
        }
    }
}