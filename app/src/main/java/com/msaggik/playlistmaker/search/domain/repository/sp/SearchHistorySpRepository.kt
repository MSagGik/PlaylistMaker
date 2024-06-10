package com.msaggik.playlistmaker.search.domain.repository.sp

import com.msaggik.playlistmaker.search.domain.models.Track

interface SearchHistorySpRepository { // интерфейс для связи data - domain
    fun clearTrackListHistoryDomain()
    fun readTrackListHistoryDomain() : List<Track>
    fun addTrackListHistoryDomain(track: Track) : List<Track>
}