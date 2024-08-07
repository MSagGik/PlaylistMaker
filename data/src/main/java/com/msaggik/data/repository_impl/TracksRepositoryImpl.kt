package com.msaggik.data.repository_impl

import android.content.Context
import com.msaggik.common_util.Resource
import com.msaggik.data.api.db.favorite_tracks.TracksDatabase
import com.msaggik.data.api.network.NetworkClient
import com.msaggik.data.api.network.dto.request.TracksSearchRequest
import com.msaggik.data.api.network.dto.response.entities.TrackResponse
import com.msaggik.data.api.network.mappers.SearchMapper
import com.msaggik.data.api.sp.search.SearchHistorySp
import com.msaggik.search.domain.model.Track
import com.msaggik.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val searchHistorySp: SearchHistorySp,
    private val tracksDatabase: TracksDatabase,
    private val converters: SearchMapper
) : TracksRepository {

    // network
    override fun searchTracks(trackSearch: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(trackSearch))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(com.msaggik.common_ui.R.string.no_connection)))
            }

            200 -> {
                emit(Resource.Success((response as TrackResponse).results.map {
                    val track = converters.convertTrackDtoToTrack(it)
                    actualizingTrack(track)
                }))
            }

            else -> {
                emit(Resource.Error(context.getString(com.msaggik.common_ui.R.string.server_error)))
            }
        }
    }

    // sp
    override suspend fun clearTrackListHistory() {
        searchHistorySp.clearTrackListHistorySharedPreferences()
    }

    override fun readTrackListHistory(): Flow<List<Track>> = flow {
        emit(
            searchHistorySp.readTrackListHistorySharedPreferences()
                .map {
                    val track = converters.convertTrackDtoToTrack(it)
                    actualizingTrack(track)
                }
        )
    }

    override fun addTrackListHistory(track: Track): Flow<List<Track>> = flow {
        val trackDto = converters.convertTrackToTrackDto(track)
        emit(
            searchHistorySp.addTrackListHistorySharedPreferences(trackDto)
                .map {
                    val trackBuf = converters.convertTrackDtoToTrack(it)
                    actualizingTrack(trackBuf)
                }
        )
    }

    private suspend fun actualizingTrack(track: Track): Track {
        val listIds = tracksDatabase.favoriteTracksDao().getFavoriteTracksIds()
        for (idFavorite in listIds) {
            val id = track.trackId.toLong()
            if (idFavorite == id) {
                track.apply { isFavorite = true }
            } else {
                track.apply { isFavorite = false }
            }
        }
        return track
    }
}