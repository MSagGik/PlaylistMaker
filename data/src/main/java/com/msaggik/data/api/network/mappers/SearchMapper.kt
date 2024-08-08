package com.msaggik.data.api.network.mappers

import com.msaggik.data.api.network.dto.response.entities.TrackDto
import com.msaggik.search.domain.model.Track

class SearchMapper {
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
}