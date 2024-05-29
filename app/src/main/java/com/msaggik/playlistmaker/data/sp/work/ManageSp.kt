package com.msaggik.playlistmaker.data.sp.work

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.data.dto.response.TrackDto
import com.msaggik.playlistmaker.util.AppConstants

interface ManageSp {
    companion object{
        fun createObjectSpTheme(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                AppConstants.SHARE_PREF_NAME,
                Application.MODE_PRIVATE
            )
        }
        fun createObjectSpSearchHistory(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                AppConstants.TRACK_LIST_PREFERENCES,
                AppCompatActivity.MODE_PRIVATE
            )
        }
    }

    fun isDarkThemeSharedPreferences() : Boolean
    fun setDarkThemeSharedPreferences(isDarkTheme : Boolean)
    fun clearTrackListHistorySharedPreferences()
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}