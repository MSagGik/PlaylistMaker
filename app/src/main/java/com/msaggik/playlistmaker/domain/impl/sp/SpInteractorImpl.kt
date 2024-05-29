package com.msaggik.playlistmaker.domain.impl.sp

import com.msaggik.playlistmaker.domain.api.sp.SpInteractor
import com.msaggik.playlistmaker.domain.api.sp.SpRepository
import com.msaggik.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class SpInteractorImpl(private val repository: SpRepository) : SpInteractor {

    val executor = Executors.newCachedThreadPool()
    override fun clearTrackListHistory() {
        executor.execute {
            repository.clearTrackListHistoryDomain()
        }
    }

    override fun readTrackListHistory(consumer: SpInteractor.SpConsumer) {
        executor.execute {
            consumer.consume(repository.readTrackListHistoryDomain())
        }
    }

    override fun addTrackListHistory(track: Track, consumer: SpInteractor.SpConsumer) {
        executor.execute {
            consumer.consume(repository.addTrackListHistoryDomain(track))
        }
    }
}