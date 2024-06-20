package com.msaggik.playlistmaker.media.data.favorite_tracks.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.msaggik.playlistmaker.media.data.dto.TrackDto
import com.msaggik.playlistmaker.media.data.favorite_tracks.FavoriteTracks
import com.msaggik.playlistmaker.util.Utils

private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"
class FavoriteTracksImpl(
    private val spFavoriteTracks: SharedPreferences,
    private val gson: Gson
) : FavoriteTracks {

    private var trackListHistory: MutableList<TrackDto> = ArrayList()

    override fun readTrackListHistorySharedPreferences(): MutableList<TrackDto> {
        trackListHistory =
            Utils.readSharedPreferences(spFavoriteTracks, TRACK_LIST_HISTORY_KEY, gson)
        return trackListHistory
    }
}