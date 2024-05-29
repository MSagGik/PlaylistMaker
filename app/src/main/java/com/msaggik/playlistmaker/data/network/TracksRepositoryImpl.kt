package com.msaggik.playlistmaker.data.network

import com.msaggik.playlistmaker.data.dto.request.TracksSearchRequest
import com.msaggik.playlistmaker.data.dto.response.TrackResponse
import com.msaggik.playlistmaker.domain.api.network.TracksRepository
import com.msaggik.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracksDomain(searchTracks: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(searchTracks))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
                Track(
                    it.trackId, it.trackName, it.artistName,
                    it.trackTimeMillis, it.artworkUrl100, it.collectionName,
                    it.releaseDate, it.primaryGenreName, it.country, it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}