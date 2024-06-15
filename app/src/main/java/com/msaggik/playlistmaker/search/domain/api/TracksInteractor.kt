package com.msaggik.playlistmaker.search.domain.api

import com.msaggik.playlistmaker.search.domain.models.Track

interface TracksInteractor { // интерфейс для связи domain - view-model
    // network
    fun searchTracks(searchTracks: String, consumer: TracksConsumer)
    interface TracksConsumer { // Callback между IO и UI потоками
        fun consume(listTracks: List<Track>?, errorMessage: String?)
    }
    // sp
    fun clearTrackListHistory()
    fun readTrackListHistory(consumer: SpTracksHistoryConsumer)
    fun addTrackListHistory(track: Track, consumer: SpTracksHistoryConsumer)
    interface SpTracksHistoryConsumer { // Callback между IO и UI потоками
        fun consume(listHistoryTracks: List<Track>)
    }
}