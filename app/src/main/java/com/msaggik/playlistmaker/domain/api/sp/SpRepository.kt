package com.msaggik.playlistmaker.domain.api.sp

import com.msaggik.playlistmaker.domain.models.Track

interface SpRepository { // интерфейс для связи data - domain
    fun clearTrackListHistoryDomain()
    fun readTrackListHistoryDomain() : List<Track>
    fun addTrackListHistoryDomain(track: Track) : List<Track>
}