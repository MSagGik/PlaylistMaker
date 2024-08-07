package com.msaggik.search.domain.repository

import com.msaggik.common_util.Resource
import com.msaggik.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    // network
    fun searchTracks(trackSearch: String): Flow<Resource<List<Track>>>
    // sp
    suspend fun clearTrackListHistory()
    fun readTrackListHistory() : Flow<List<Track>>
    fun addTrackListHistory(track: Track) : Flow<List<Track>>
}