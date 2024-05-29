package com.msaggik.playlistmaker.domain.impl

import com.msaggik.playlistmaker.domain.api.TracksInteractor
import com.msaggik.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(searchTracks: String, consumer: TracksInteractor.TracksConsumer) {
        Executors.newCachedThreadPool().execute {
            consumer.consume(repository.searchTracks(searchTracks))
        }
    }
}