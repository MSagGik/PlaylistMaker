package com.msaggik.playlistmaker.search.domain.api.impl

import com.msaggik.playlistmaker.search.domain.api.TracksInteractor
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    val executor = Executors.newCachedThreadPool()

    // network
    override fun searchTracks(searchTracks: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracksDomain(searchTracks)) {
                is Resource.Success -> { consumer.consume(resource.data, null)}
                is Resource.Error -> { consumer.consume(null, resource.message)}
            }
        }
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