package com.msaggik.playlistmaker.domain.api.network

import com.msaggik.playlistmaker.domain.models.Track

interface TracksRepository { // интерфейс для связи data - domain
    fun searchTracksDomain(trackSearch: String): List<Track>
}