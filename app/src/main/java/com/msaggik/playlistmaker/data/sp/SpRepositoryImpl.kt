package com.msaggik.playlistmaker.data.sp

import com.msaggik.playlistmaker.data.dto.response.TrackDto
import com.msaggik.playlistmaker.data.sp.work.SearchHistory
import com.msaggik.playlistmaker.data.sp.work.SearchHistoryImpl
import com.msaggik.playlistmaker.domain.api.sp.SpRepository
import com.msaggik.playlistmaker.domain.models.Track

class SpRepositoryImpl(private val searchHistory: SearchHistory) : SpRepository{

    override fun clearTrackListHistoryDomain() {
        (searchHistory as SearchHistoryImpl).clearTrackListHistorySharedPreferences()
    }

    override fun readTrackListHistoryDomain() : List<Track> {
        return (searchHistory as SearchHistoryImpl).readTrackListHistorySharedPreferences().map {
            Track(it.trackId, it.trackName, it.artistName,
            it.trackTimeMillis, it.artworkUrl100, it.collectionName,
            it.releaseDate, it.primaryGenreName, it.country, it.previewUrl
        )}
    }

    override fun addTrackListHistoryDomain(track: Track) : List<Track> {
        val trackDto = TrackDto(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
        val trackListHistory = (searchHistory as SearchHistoryImpl).addTrackListHistorySharedPreferences(trackDto)
        return trackListHistory.map {
            Track(it.trackId, it.trackName, it.artistName,
                it.trackTimeMillis, it.artworkUrl100, it.collectionName,
                it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
        }
    }
}