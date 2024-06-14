package com.msaggik.playlistmaker.search.data.base.sp.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.dto.response.TrackDto

private const val TRACK_LIST_LIMIT = 10
private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"
class SearchHistorySpImpl (
//    private val context: Context,
    private val spSearchHistory: SharedPreferences
) : SearchHistorySp {

    private var trackListHistory: MutableList<TrackDto> = ArrayList()
//    private val spSearchHistory = SearchHistorySp.createObjectSpSearchHistory(context)

    override fun clearTrackListHistorySharedPreferences() {
        spSearchHistory.edit()
            .clear()
            .apply()
        trackListHistory.clear()
    }

    override fun readTrackListHistorySharedPreferences(): MutableList<TrackDto> {
        readSharePreferences(spSearchHistory)
        return trackListHistory
    }

    override fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto> {
        readSharePreferences(spSearchHistory)
        addTrackListHistory(track)
        writeSharePreferences(spSearchHistory)
        return trackListHistory
    }

    private fun readSharePreferences(sharedPreferences: SharedPreferences) {
        val json = sharedPreferences.getString(TRACK_LIST_HISTORY_KEY, null)
        if(json != null) {
            trackListHistory.clear()
            trackListHistory = Gson().fromJson(json, Array<TrackDto>::class.java).toMutableList()
        }
    }

    private fun addTrackListHistory(track: TrackDto) {
        var unique = true
        var isNotFirst = true
        for(i in 0..<trackListHistory.size) {
            if (trackListHistory[i].trackId == track.trackId) {
                unique = false
                if(i == 0) {
                    isNotFirst = false
                }
            }
        }

        if(unique) {
            trackListHistory.add(0, track)
            if(trackListHistory.size > TRACK_LIST_LIMIT) {
                trackListHistory.removeLast()
            }
        } else if(isNotFirst){
            trackListHistory.remove(track)
            trackListHistory.add(0, track)
        }
    }

    private fun writeSharePreferences(sharedPreferences: SharedPreferences) {
        val json = Gson().toJson(trackListHistory)
        sharedPreferences.edit()
            .putString(TRACK_LIST_HISTORY_KEY, json)
            .apply()
    }
}