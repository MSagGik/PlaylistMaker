package com.msaggik.playlistmaker.domain.api.network

import com.msaggik.playlistmaker.domain.models.Track

interface TracksInteractor { // интерфейс для связи domain - presentation
    fun searchTracks(searchTracks: String, consumer: TracksConsumer)

    interface TracksConsumer { // Callback между IO и UI потоками
        fun consume(listTracks: List<Track>)
    }
}