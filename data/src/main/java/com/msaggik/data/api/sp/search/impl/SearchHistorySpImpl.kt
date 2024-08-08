package com.msaggik.data.api.sp.search.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.msaggik.data.api.network.dto.response.entities.TrackDto
import com.msaggik.data.api.sp.search.SearchHistorySp
import com.msaggik.data.local_util.Utils

private const val TRACK_LIST_LIMIT = 10
private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"
class SearchHistorySpImpl (
    private val spSearchHistory: SharedPreferences,
    private val gson: Gson
) : SearchHistorySp {

    private var trackListHistory: MutableList<TrackDto> = ArrayList()

    override suspend fun clearTrackListHistorySharedPreferences() {
        spSearchHistory.edit()
            .clear()
            .apply()
        trackListHistory.clear()
    }

    override suspend fun readTrackListHistorySharedPreferences(): MutableList<TrackDto> {
        trackListHistory = Utils.readSharedPreferences(spSearchHistory, TRACK_LIST_HISTORY_KEY, gson)
        return trackListHistory
    }

    override suspend fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto> {
        trackListHistory = Utils.addTrackInList(
            track = track,
            trackList = readTrackListHistorySharedPreferences(),
            limit = TRACK_LIST_LIMIT
        )
        Utils.writeSharedPreferences(spSearchHistory, TRACK_LIST_HISTORY_KEY, trackListHistory, gson)
        return trackListHistory
    }
}