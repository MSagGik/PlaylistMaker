package com.msaggik.playlistmaker.search.data.repository.network

import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Resource

interface TracksRepository { // интерфейс для связи data - domain
    fun searchTracksDomain(trackSearch: String): Resource<List<Track>>
}