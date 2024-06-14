package com.msaggik.playlistmaker.search.domain.api.network.impl

import com.msaggik.playlistmaker.search.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.search.domain.repository.network.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {
    override fun searchTracks(searchTracks: String, consumer: TracksInteractor.TracksConsumer) {
        Executors.newCachedThreadPool().execute {
            when(val resource = repository.searchTracksDomain(searchTracks)) {
                is Resource.Success -> { consumer.consume(resource.data, null)}
                is Resource.Error -> { consumer.consume(null, resource.message)}
            }
        }
    }
}