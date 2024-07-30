package com.msaggik.playlistmaker.media.data.repository_impl

import com.msaggik.playlistmaker.media.data.favorite_tracks.FavoriteTracks
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val favoriteTracks: FavoriteTracks
) : MediaRepository {
    override fun readTrackListHistory(): List<Track> {
        return favoriteTracks.readTrackListHistorySharedPreferences()
            .map {
                with(it) {
                    Track(
                        trackId, trackName, artistName,
                        trackTimeMillis, artworkUrl100, collectionName,
                        releaseDate, primaryGenreName, country, previewUrl
                    )
                }
            }
    }
}