package com.msaggik.playlistmaker.search.data.repository_impl

import android.content.Context
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import com.msaggik.playlistmaker.search.data.base.network.NetworkClient
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.converters.ConvertersSearch
import com.msaggik.playlistmaker.search.data.dto.request.TracksSearchRequest
import com.msaggik.playlistmaker.search.data.dto.response.TrackResponse
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val searchHistorySp: SearchHistorySp,
    private val database: PlaylistTracksDatabase,
    private val converters: ConvertersSearch
) : TracksRepository {

    // network
    override fun searchTracks(trackSearch: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(trackSearch))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.no_connection)))
            }

            200 -> {
                emit(Resource.Success((response as TrackResponse).results.map {
                    val track = converters.convertTrackDtoToTrack(it)
                    actualizingTrack(track)
                }))
            }

            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
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
        val listIds = database.playlistTracksDao().getFavoriteTracksIds()
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