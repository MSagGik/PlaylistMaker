package com.msaggik.playlistmaker.search.data.repository_impl

import android.content.Context
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.search.data.base.network.NetworkClient
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.dto.request.TracksSearchRequest
import com.msaggik.playlistmaker.search.data.dto.response.TrackResponse
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import com.msaggik.playlistmaker.util.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl (
    private val context: Context,
    private val networkClient: NetworkClient,
    private val searchHistorySp: SearchHistorySp
) : TracksRepository {

    // network
    override fun searchTracks(trackSearch: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(trackSearch))
        when(response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.no_connection)))
            }
            200 -> {
                emit(Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId, it.trackName, it.artistName,
                        it.trackTimeMillis, it.artworkUrl100, it.collectionName,
                        it.releaseDate, it.primaryGenreName, it.country, it.previewUrl
                    )
                }))
            }
            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }

    // sp
    override fun clearTrackListHistory() {
        searchHistorySp.clearTrackListHistorySharedPreferences()
    }

    override fun readTrackListHistory(): List<Track> {
        return searchHistorySp.readTrackListHistorySharedPreferences()
            .map {
                Utils.convertTrackDtoToTrack(it)
            }
    }

    override fun addTrackListHistory(track: Track): List<Track> {
        val trackDto = Utils.convertTrackToTrackDto(track)
        val trackListHistory = searchHistorySp.addTrackListHistorySharedPreferences(trackDto)
        return trackListHistory.map {
            Utils.convertTrackDtoToTrack(it)
        }
    }
}