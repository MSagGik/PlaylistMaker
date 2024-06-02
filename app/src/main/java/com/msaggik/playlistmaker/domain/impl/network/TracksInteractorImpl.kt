package com.msaggik.playlistmaker.domain.impl.network

import com.msaggik.playlistmaker.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.domain.api.network.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(searchTracks: String, consumer: TracksInteractor.TracksConsumer) {
        Executors.newCachedThreadPool().execute {
            consumer.consume(repository.searchTracksDomain(searchTracks))
        }
    }
}