package com.msaggik.playlistmaker.search.data.converters

import com.msaggik.playlistmaker.search.data.dto.response.TrackDto
import com.msaggik.playlistmaker.search.domain.models.Track

class ConvertersSearch {
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