package com.msaggik.playlistmaker.domain.api.sp

import com.msaggik.playlistmaker.data.dto.response.TrackDto
import com.msaggik.playlistmaker.domain.models.Track

interface SpRepository { // интерфейс для связи data - domain

    fun isDarkThemeDomain() : Boolean
    fun setDarkThemeDomain(isDarkTheme : Boolean)

    fun clearTrackListHistoryDomain()
    fun readTrackListHistoryDomain() : List<Track>
    fun addTrackListHistoryDomain(track: Track) : List<Track>
}