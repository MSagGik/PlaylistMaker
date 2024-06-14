package com.msaggik.playlistmaker.search.data.repository_impl.sp

import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.sp.SearchHistorySpRepository
import com.msaggik.playlistmaker.util.Utils

class SearchHistorySpRepositoryImpl(
    private val searchHistorySp: SearchHistorySp
) :
    SearchHistorySpRepository {

    override fun clearTrackListHistoryDomain() {
        (searchHistorySp as SearchHistorySpImpl).clearTrackListHistorySharedPreferences()
    }

    override fun readTrackListHistoryDomain(): List<Track> {
        return (searchHistorySp as SearchHistorySpImpl).readTrackListHistorySharedPreferences()
            .map {
                Utils.convertTrackDtoToTrack(it)
            }
    }

    override fun addTrackListHistoryDomain(track: Track): List<Track> {
        val trackDto = Utils.convertTrackToTrackDto(track)
        val trackListHistory =
            (searchHistorySp as SearchHistorySpImpl).addTrackListHistorySharedPreferences(trackDto)
        return trackListHistory.map {
            Utils.convertTrackDtoToTrack(it)
        }
    }
}