package com.msaggik.playlistmaker.search.data.base.sp

import com.msaggik.playlistmaker.media.data.dto.TrackDto

interface SearchHistorySp {
    fun clearTrackListHistorySharedPreferences()
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}