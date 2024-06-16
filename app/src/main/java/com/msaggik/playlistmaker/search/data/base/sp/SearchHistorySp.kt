package com.msaggik.playlistmaker.search.data.base.sp

import com.msaggik.playlistmaker.search.data.dto.response.TrackDto

interface SearchHistorySp {
    fun clearTrackListHistorySharedPreferences()
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}