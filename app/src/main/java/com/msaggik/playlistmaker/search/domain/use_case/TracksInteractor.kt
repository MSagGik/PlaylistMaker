package com.msaggik.playlistmaker.search.domain.use_case

import com.msaggik.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor { // интерфейс для связи domain - view-model
    // network
    fun searchTracks(searchTracks: String): Flow<Pair<List<Track>?, String?>>
    // sp
    fun clearTrackListHistory()
    fun readTrackListHistory(consumer: SpTracksHistoryConsumer)
    fun addTrackListHistory(track: Track, consumer: SpTracksHistoryConsumer)
    interface SpTracksHistoryConsumer { // Callback между IO и UI потоками
        fun consume(listHistoryTracks: List<Track>)
    }
}