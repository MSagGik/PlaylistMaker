package com.msaggik.data.api.sp.search

import com.msaggik.data.api.network.dto.response.entities.TrackDto

interface SearchHistorySp {
    suspend fun clearTrackListHistorySharedPreferences()
    suspend fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    suspend fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}