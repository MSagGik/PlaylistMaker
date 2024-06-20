package com.msaggik.playlistmaker.media.data.favorite_tracks

import com.msaggik.playlistmaker.media.data.dto.TrackDto

interface FavoriteTracks {
    fun readTrackListHistorySharedPreferences() : MutableList<TrackDto>
}