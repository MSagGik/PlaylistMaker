package com.msaggik.data.local_util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.msaggik.data.api.network.dto.response.entities.TrackDto

object Utils {

    fun addTrackInList(track: TrackDto, trackList: MutableList<TrackDto>, limit: Int): MutableList<TrackDto> {
        var unique = true
        var isNotFirst = true
        for(i in 0..<trackList.size) {
            if (trackList[i].trackId == track.trackId) {
                unique = false
                if(i == 0) {
                    isNotFirst = false
                }
            }
        }
        if(unique) {
            trackList.add(0, track)
            if(trackList.size > limit) {
                trackList.removeLast()
            }
        } else if(isNotFirst){
            trackList.remove(track)
            trackList.add(0, track)
        }
        return trackList
    }

    fun searchTrackInList(trackId: Int, list: MutableList<TrackDto>) : TrackDto {
        return list.firstOrNull { track -> track.trackId == trackId } ?: TrackDto()
    }

    fun readSharedPreferences(sharedPreferences: SharedPreferences, trackListKey: String, gson: Gson): MutableList<TrackDto> {
        val trackList: MutableList<TrackDto> = ArrayList()
        val json = sharedPreferences.getString(trackListKey, null)
        if(json != null) {
            trackList.addAll(gson.fromJson(json, Array<TrackDto>::class.java))
        }
        return trackList
    }

    fun writeSharedPreferences(sharedPreferences: SharedPreferences, trackListKey: String, trackList: MutableList<TrackDto>, gson: Gson) {
        val json = gson.toJson(trackList)
        sharedPreferences.edit()
            .putString(trackListKey, json)
            .apply()
    }
}