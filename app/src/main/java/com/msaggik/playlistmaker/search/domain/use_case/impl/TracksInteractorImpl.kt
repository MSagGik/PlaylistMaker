package com.msaggik.playlistmaker.search.domain.use_case.impl

import com.msaggik.playlistmaker.search.domain.use_case.TracksInteractor
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    // network
    override fun searchTracks(searchTracks: String) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(searchTracks).map { resource -> Resource.handleResource(resource) }
    }

    // sp
    val executor = Executors.newCachedThreadPool()

    override fun clearTrackListHistory() {
        executor.execute {
            repository.clearTrackListHistory()
        }
    }

    override fun readTrackListHistory(consumer: TracksInteractor.SpTracksHistoryConsumer) {
        executor.execute {
            consumer.consume(repository.readTrackListHistory())
        }
    }

    override fun addTrackListHistory(track: Track, consumer: TracksInteractor.SpTracksHistoryConsumer) {
        executor.execute {
            consumer.consume(repository.addTrackListHistory(track))
        }
    }
}