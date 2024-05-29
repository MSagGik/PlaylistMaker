package com.msaggik.playlistmaker.domain.api.sp

import com.msaggik.playlistmaker.domain.models.Track

interface SpInteractor { // интерфейс для связи domain - presentation

    fun clearTrackListHistory()
    fun readTrackListHistory(consumer: SpConsumer)
    fun addTrackListHistory(track: Track, consumer: SpConsumer)

    interface SpConsumer { // Callback между IO и UI потоками
        fun consume(listHistoryTracks: List<Track>)
    }
}