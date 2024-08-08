package com.msaggik.playlistmaker.search.data.base.sp

import com.msaggik.playlistmaker.search.data.dto.response.TrackDto

interface SearchHistorySp {
    suspend fun clearTrackListHistorySharedPreferences()
    suspend fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    suspend fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}