package com.msaggik.playlistmaker.domain.api.sp

import com.msaggik.playlistmaker.domain.models.Track

interface SpInteractor { // интерфейс для связи domain - presentation

    fun isDarkTheme(consumer: SpThemeConsumer)
    fun setDarkTheme(isDarkTheme : Boolean)

    fun clearTrackListHistory()
    fun readTrackListHistory(consumer: SpTracksHistoryConsumer)
    fun addTrackListHistory(track: Track, consumer: SpTracksHistoryConsumer)

    // Callback между IO и UI потоками
    interface SpThemeConsumer {
        fun consume(isDarkTheme : Boolean)
    }
    interface SpTracksHistoryConsumer {
        fun consume(listHistoryTracks: List<Track>)
    }
}