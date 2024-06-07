package com.msaggik.playlistmaker.search.data.repository.network.impl

import android.content.Context
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.search.data.dto.request.TracksSearchRequest
import com.msaggik.playlistmaker.search.data.dto.response.TrackResponse
import com.msaggik.playlistmaker.search.data.base.network.NetworkClient
import com.msaggik.playlistmaker.search.data.repository.network.TracksRepository
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context
    ) : TracksRepository {
    override fun searchTracksDomain(trackSearch: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(trackSearch))
        return when(response.resultCode) {
            -1 -> {
                Resource.Error(context.getString(R.string.no_connection))
            }
            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId, it.trackName, it.artistName,
                        it.trackTimeMillis, it.artworkUrl100, it.collectionName,
                        it.releaseDate, it.primaryGenreName, it.country, it.previewUrl
                    )
                })
            }
            else -> {
                Resource.Error(context.getString(R.string.server_error))
            }
        }
    }
}