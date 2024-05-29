package com.msaggik.playlistmaker.domain.api

import com.msaggik.playlistmaker.domain.models.Track

interface TracksRepository { // интерфейс для связи data - domain
    fun searchTracks(trackSearch: String): List<Track>
}