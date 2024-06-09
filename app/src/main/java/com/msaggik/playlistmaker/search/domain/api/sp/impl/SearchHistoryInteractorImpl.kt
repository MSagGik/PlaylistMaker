package com.msaggik.playlistmaker.search.domain.api.sp.impl

import com.msaggik.playlistmaker.search.domain.api.sp.SearchHistoryInteractor
import com.msaggik.playlistmaker.search.domain.repository.sp.SearchHistorySpRepository
import com.msaggik.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class SearchHistoryInteractorImpl(private val repository: SearchHistorySpRepository) : SearchHistoryInteractor {

    val executor = Executors.newCachedThreadPool()

    override fun clearTrackListHistory() {
        executor.execute {
            repository.clearTrackListHistoryDomain()
        }
    }

    override fun readTrackListHistory(consumer: SearchHistoryInteractor.SpTracksHistoryConsumer) {
        executor.execute {
            consumer.consume(repository.readTrackListHistoryDomain())
        }
    }

    override fun addTrackListHistory(track: Track, consumer: SearchHistoryInteractor.SpTracksHistoryConsumer) {
        executor.execute {
            consumer.consume(repository.addTrackListHistoryDomain(track))
        }
    }
}