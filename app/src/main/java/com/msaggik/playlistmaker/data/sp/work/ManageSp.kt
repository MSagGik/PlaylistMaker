package com.msaggik.playlistmaker.data.sp.work

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.data.dto.response.TrackDto

private const val SHARE_PREF_NAME = "share_pref_name"
private const val TRACK_LIST_PREFERENCES = "track_list_preferences"
interface ManageSp {
    companion object{
        fun createObjectSpTheme(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARE_PREF_NAME, Application.MODE_PRIVATE)
        }
        fun createObjectSpSearchHistory(context: Context): SharedPreferences {
            return context.getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        }
    }

    fun isDarkThemeSharedPreferences() : Boolean
    fun setDarkThemeSharedPreferences(isDarkTheme : Boolean)
    fun clearTrackListHistorySharedPreferences()
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
    fun addTrackListHistorySharedPreferences(track: TrackDto) : MutableList<TrackDto>
}