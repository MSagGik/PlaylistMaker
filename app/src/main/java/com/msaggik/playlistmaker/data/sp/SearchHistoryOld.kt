package com.msaggik.playlistmaker.data.sp

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.msaggik.playlistmaker.domain.models.Track
import com.msaggik.playlistmaker.util.AppConstants

class SearchHistoryOld {

    private var trackListHistory: MutableList<Track> = ArrayList()

    fun clearTrackListHistorySharedPreferences(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .clear()
            .apply()
        trackListHistory.clear()
        Log.e("clearTrackListHistory", "clearTrackListHistory")
    }

    fun readTrackListHistorySharedPreferences(sharedPreferences: SharedPreferences): MutableList<Track> {
        readSharePreferences(sharedPreferences)
        Log.e("readTrackListHistory", "size " + trackListHistory.size)
        return trackListHistory
    }

    fun addTrackListHistorySharedPreferences(sharedPreferences: SharedPreferences, track: Track) {
        Log.e("addTrackListHistory", "add start size " + trackListHistory.size + ", " + track.trackName)
        readSharePreferences(sharedPreferences)
        addTrackListHistory(track)
        writeSharePreferences(sharedPreferences)
        Log.e("addTrackListHistory", "add end size " + trackListHistory.size + ", " + track.trackName)
    }

    private fun readSharePreferences(sharedPreferences: SharedPreferences) {
        val json = sharedPreferences.getString(AppConstants.TRACK_LIST_HISTORY_KEY, null)
        if(json != null) {
            trackListHistory.clear()
            trackListHistory = Gson().fromJson(json, Array<Track>::class.java).toMutableList()
        }
        Log.e("readSharePreferences", "read " + trackListHistory.size)
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
            if(trackListHistory.size > AppConstants.TRACK_LIST_LIMIT) {
                trackListHistory.removeLast()
            }
        } else if(isNotFirst){
            trackListHistory.remove(track)
            trackListHistory.add(0, track)
        }
        Log.e("addTrackListHistory1", "add " + track.trackName)
    }

    private fun writeSharePreferences(sharedPreferences: SharedPreferences) {
        val json = Gson().toJson(trackListHistory)
        sharedPreferences.edit()
            .putString(AppConstants.TRACK_LIST_HISTORY_KEY, json)
            .apply()
        Log.e("writeSharePreferences", "write " + trackListHistory.size)
    }
}