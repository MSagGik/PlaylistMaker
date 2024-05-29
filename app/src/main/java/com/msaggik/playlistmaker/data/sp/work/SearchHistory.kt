package com.msaggik.playlistmaker.data.sp.work

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.data.dto.response.TrackDto
import com.msaggik.playlistmaker.util.AppConstants

interface SearchHistory {
    companion object{
        fun createObjectSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                AppConstants.TRACK_LIST_PREFERENCES,
                AppCompatActivity.MODE_PRIVATE
            )
        }
    }

    fun clearTrackListHistorySharedPreferences()
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}