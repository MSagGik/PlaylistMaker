package com.msaggik.playlistmaker.search.domain.use_case

import com.msaggik.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor { // интерфейс для связи domain - view-model
    // network
    fun searchTracks(searchTracks: String): Flow<Pair<List<Track>?, String?>>
    // sp
    suspend fun clearTrackListHistory()
    fun readTrackListHistory() : Flow<List<Track>>
    fun addTrackListHistory(track: Track) : Flow<List<Track>>
}