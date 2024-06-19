package com.msaggik.playlistmaker.media.domain.api

import com.msaggik.playlistmaker.media.domain.models.Track

interface MediaInteractor {
    fun readTrackListMedia(consumer: MediaConsumer)
    interface MediaConsumer { // Callback между IO и UI потоками
        fun consume(listTracks: List<Track>)
    }
}