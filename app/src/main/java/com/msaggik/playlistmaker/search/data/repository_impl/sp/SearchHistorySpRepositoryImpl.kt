package com.msaggik.playlistmaker.search.data.repository_impl.sp

import com.msaggik.playlistmaker.search.data.dto.response.TrackDto
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.sp.SearchHistorySpRepository

class SearchHistorySpRepositoryImpl(private val manageSp: SearchHistorySp) :
    SearchHistorySpRepository {

    override fun clearTrackListHistoryDomain() {
        (manageSp as SearchHistorySpImpl).clearTrackListHistorySharedPreferences()
    }

    override fun readTrackListHistoryDomain() : List<Track> {
        return (manageSp as SearchHistorySpImpl).readTrackListHistorySharedPreferences().map {
            Track(it.trackId, it.trackName, it.artistName,
            it.trackTimeMillis, it.artworkUrl100, it.collectionName,
            it.releaseDate, it.primaryGenreName, it.country, it.previewUrl
        )
        }
    }

    override fun addTrackListHistoryDomain(track: Track) : List<Track> {
        val trackDto = TrackDto(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
        val trackListHistory = (manageSp as SearchHistorySpImpl).addTrackListHistorySharedPreferences(trackDto)
        return trackListHistory.map {
            Track(it.trackId, it.trackName, it.artistName,
                it.trackTimeMillis, it.artworkUrl100, it.collectionName,
                it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
        }
    }
}