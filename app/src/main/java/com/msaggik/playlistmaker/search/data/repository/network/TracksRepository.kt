package com.msaggik.playlistmaker.search.data.repository.network

import com.msaggik.playlistmaker.search.domain.models.Track

interface TracksRepository { // интерфейс для связи data - domain
    fun searchTracksDomain(trackSearch: String): List<Track>
}