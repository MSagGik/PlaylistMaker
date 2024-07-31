package com.msaggik.playlistmaker.search.domain.repository

import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    // network
    fun searchTracks(trackSearch: String): Flow<Resource<List<Track>>>
    // sp
    fun clearTrackListHistory()
    fun readTrackListHistory() : List<Track>
    fun addTrackListHistory(track: Track) : List<Track>
}