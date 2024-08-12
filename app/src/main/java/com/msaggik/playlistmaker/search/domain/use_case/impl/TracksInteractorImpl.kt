package com.msaggik.playlistmaker.search.domain.use_case.impl

import com.msaggik.playlistmaker.search.domain.use_case.TracksInteractor
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    // network
    override fun searchTracks(searchTracks: String) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(searchTracks).map { resource -> Resource.handleResource(resource) }
    }

    // sp

    override suspend fun clearTrackListHistory() {
        repository.clearTrackListHistory()
    }

    override fun readTrackListHistory() : Flow<List<Track>> {
        return repository.readTrackListHistory()
    }

    override fun addTrackListHistory(track: Track) : Flow<List<Track>> {
        return repository.addTrackListHistory(track)
    }
}