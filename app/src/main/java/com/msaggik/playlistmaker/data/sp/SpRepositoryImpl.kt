package com.msaggik.playlistmaker.data.sp

import com.msaggik.playlistmaker.data.dto.response.TrackDto
import com.msaggik.playlistmaker.data.sp.work.ManageSp
import com.msaggik.playlistmaker.data.sp.work.ManageSpImpl
import com.msaggik.playlistmaker.domain.api.sp.SpRepository
import com.msaggik.playlistmaker.domain.models.Track

class SpRepositoryImpl(private val manageSp: ManageSp) : SpRepository{
    override fun isDarkThemeDomain(): Boolean {
        return (manageSp as ManageSpImpl).isDarkThemeSharedPreferences()
    }

    override fun setDarkThemeDomain(isDarkTheme: Boolean) {
        (manageSp as ManageSpImpl).setDarkThemeSharedPreferences(isDarkTheme)
    }

    override fun clearTrackListHistoryDomain() {
        (manageSp as ManageSpImpl).clearTrackListHistorySharedPreferences()
    }

    override fun readTrackListHistoryDomain() : List<Track> {
        return (manageSp as ManageSpImpl).readTrackListHistorySharedPreferences().map {
            Track(it.trackId, it.trackName, it.artistName,
            it.trackTimeMillis, it.artworkUrl100, it.collectionName,
            it.releaseDate, it.primaryGenreName, it.country, it.previewUrl
        )}
    }

    override fun addTrackListHistoryDomain(track: Track) : List<Track> {
        val trackDto = TrackDto(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
        val trackListHistory = (manageSp as ManageSpImpl).addTrackListHistorySharedPreferences(trackDto)
        return trackListHistory.map {
            Track(it.trackId, it.trackName, it.artistName,
                it.trackTimeMillis, it.artworkUrl100, it.collectionName,
                it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
        }
    }
}