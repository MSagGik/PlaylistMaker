package com.msaggik.playlistmaker.search.domain.api.impl

import com.msaggik.playlistmaker.search.domain.api.TracksInteractor
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    val executor = Executors.newCachedThreadPool()

    // network
    override fun searchTracks(searchTracks: String) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracksDomain(searchTracks).map { resource -> Resource.handleResource(resource) }
    }

    // sp
    override fun clearTrackListHistory() {
        executor.execute {
            repository.clearTrackListHistoryDomain()
        }
    }

    override fun readTrackListHistory(consumer: TracksInteractor.SpTracksHistoryConsumer) {
        executor.execute {
            consumer.consume(repository.readTrackListHistoryDomain())
        }
    }

    override fun addTrackListHistory(track: Track, consumer: TracksInteractor.SpTracksHistoryConsumer) {
        executor.execute {
            consumer.consume(repository.addTrackListHistoryDomain(track))
        }
    }
}