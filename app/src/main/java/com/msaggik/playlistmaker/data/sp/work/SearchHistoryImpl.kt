package com.msaggik.playlistmaker.data.sp.work

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.msaggik.playlistmaker.data.dto.response.TrackDto
import com.msaggik.playlistmaker.presentation.ui.activity.App
import com.msaggik.playlistmaker.util.AppConstants

class SearchHistoryImpl (context: Context) : SearchHistory {

    private var trackListHistory: MutableList<TrackDto> = ArrayList()
    private val sharedPreferences = SearchHistory.createObjectSharedPreferences(context)
    override fun clearTrackListHistorySharedPreferences() {
        sharedPreferences.edit()
            .clear()
            .apply()
        trackListHistory.clear()
    }

    override fun readTrackListHistorySharedPreferences(): MutableList<TrackDto> {
        readSharePreferences(sharedPreferences)
        return trackListHistory
    }

    override fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto> {
        readSharePreferences(sharedPreferences)
        addTrackListHistory(track)
        writeSharePreferences(sharedPreferences)
        return trackListHistory
    }

    private fun readSharePreferences(sharedPreferences: SharedPreferences) {
        val json = sharedPreferences.getString(AppConstants.TRACK_LIST_HISTORY_KEY, null)
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
            if(trackListHistory.size > AppConstants.TRACK_LIST_LIMIT) {
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
            .putString(AppConstants.TRACK_LIST_HISTORY_KEY, json)
            .apply()
    }
}