package com.msaggik.playlistmaker.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.msaggik.playlistmaker.search.data.dto.response.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

internal object Utils {
    fun doToPx(dp: Float, context: Context): Int {
        return TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
            .toInt()
    }

    fun visibilityView(views: Array<View>? = emptyArray(), v: View) {
        if (views != null) {
            for(view in views) view.visibility = View.GONE
        }
        v.visibility = View.VISIBLE
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormatStandardToYear(dateFormatStandard: String) : String? {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateFormatStandard)
            ?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
    }

    fun dateFormatNameAlbumPlaylist(dateFormatMillis: Long) : String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(roundingToThousandths(dateFormatMillis))
        return "Album_$dateFormat"
    }

    fun dateFormatMillisToMinSecShort(dateFormatMillis: Long) : String? {
        return SimpleDateFormat("m:ss", Locale.getDefault()).format(roundingToThousandths(dateFormatMillis))
    }

    fun dateFormatMillisToMinSecFull(dateFormatMillis: Long) : String? {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(roundingToThousandths(dateFormatMillis))
    }

    fun setApplicationTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun roundingToThousandths(inputValue: Long) : Long {
        var value = inputValue
        val remainder = value % 1000
        if(remainder > 401) {
            value += 1000 - remainder
        }
        return value
    }

    fun searchTrackInList(trackId: Int, list: MutableList<TrackDto>) : TrackDto {
        return list.firstOrNull { track -> track.trackId == trackId } ?: TrackDto()
    }

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