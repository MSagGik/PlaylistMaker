package com.msaggik.playlistmaker.search.domain.api.sp

import com.msaggik.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor { // // интерфейс для связи domain - view-model

    fun clearTrackListHistory()
    fun readTrackListHistory(consumer: SpTracksHistoryConsumer)
    fun addTrackListHistory(track: Track, consumer: SpTracksHistoryConsumer)

    // Callback между IO и UI потоками
    interface SpTracksHistoryConsumer {
        fun consume(listHistoryTracks: List<Track>)
    }
}