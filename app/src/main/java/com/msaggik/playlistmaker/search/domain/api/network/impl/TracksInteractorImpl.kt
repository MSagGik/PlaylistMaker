package com.msaggik.playlistmaker.search.domain.api.network.impl

import com.msaggik.playlistmaker.search.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.search.data.repository.network.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(searchTracks: String, consumer: TracksInteractor.TracksConsumer) {
        Executors.newCachedThreadPool().execute {
            consumer.consume(repository.searchTracksDomain(searchTracks))
        }
    }
}