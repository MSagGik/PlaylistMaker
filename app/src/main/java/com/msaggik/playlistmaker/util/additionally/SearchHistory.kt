package com.msaggik.playlistmaker.util.additionally

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.msaggik.playlistmaker.entity.Track

private const val TRACK_LIST_LIMIT = 10
private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"

class SearchHistory {

    private var trackListHistory: MutableList<Track> = ArrayList()

    fun clearTrackListHistorySharedPreferences(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .clear()
            .apply()
        trackListHistory.clear()
    }

    fun readTrackListHistorySharedPreferences(sharedPreferences: SharedPreferences): MutableList<Track> {
        readSharePreferences(sharedPreferences)
        return trackListHistory
    }

    fun addTrackListHistorySharedPreferences(sharedPreferences: SharedPreferences, track: Track) {
        readSharePreferences(sharedPreferences)
        addTrackListHistory(track)
        writeSharePreferences(sharedPreferences)
    }

    private fun readSharePreferences(sharedPreferences: SharedPreferences) {
        val json = sharedPreferences.getString(TRACK_LIST_HISTORY_KEY, null)
        if(json != null) {
            trackListHistory.clear()
            trackListHistory = Gson().fromJson(json, Array<Track>::class.java).toMutableList()
        }
    }

    private fun addTrackListHistory(track: Track) {
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