package com.msaggik.playlistmaker.search.domain.api.network

import com.msaggik.playlistmaker.search.domain.models.Track

interface TracksInteractor { // интерфейс для связи domain - view-model
    fun searchTracks(searchTracks: String, consumer: TracksConsumer)

    interface TracksConsumer { // Callback между IO и UI потоками
        fun consume(listTracks: List<Track>?, errorMessage: String?)
    }
}