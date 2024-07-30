package com.msaggik.playlistmaker.search.domain.repository

import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    // network
    fun searchTracksDomain(trackSearch: String): Flow<Resource<List<Track>>>
    // sp
    fun clearTrackListHistoryDomain()
    fun readTrackListHistoryDomain() : List<Track>
    fun addTrackListHistoryDomain(track: Track) : List<Track>
}