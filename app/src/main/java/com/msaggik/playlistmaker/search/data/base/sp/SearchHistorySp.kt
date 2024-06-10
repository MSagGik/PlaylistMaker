package com.msaggik.playlistmaker.search.data.base.sp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.search.data.dto.response.TrackDto

private const val TRACK_LIST_PREFERENCES = "track_list_preferences"
interface SearchHistorySp {
    companion object{
        fun createObjectSpSearchHistory(context: Context): SharedPreferences {
            return context.getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        }
    }
    fun clearTrackListHistorySharedPreferences()
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}