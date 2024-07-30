package com.msaggik.playlistmaker.search.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.search.domain.api.TracksInteractor
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.ui.state.TracksState
import com.msaggik.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel (
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    // trackListHistoryLiveData
    private val trackListHistoryLiveData = MutableLiveData<List<Track>>()

    init {
        readTrackListHistory()
    }

    private fun readTrackListHistory() {
        tracksInteractor.readTrackListHistory(object : TracksInteractor.SpTracksHistoryConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(listHistoryTracks: List<Track>) {
                trackListHistoryLiveData.postValue(listHistoryTracks)
            }
        })
    }

    fun addTrackListHistory(track: Track) {
        tracksInteractor.addTrackListHistory(
            track,
            object : TracksInteractor.SpTracksHistoryConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(listHistoryTracks: List<Track>) {
                    trackListHistoryLiveData.postValue(listHistoryTracks)
                }
            })
    }

    fun clearTrackListHistory() {
        tracksInteractor.clearTrackListHistory()
    }

    fun getTrackListHistoryLiveData(): LiveData<List<Track>> = trackListHistoryLiveData

    // stateLiveData
    private val stateLiveData = MutableLiveData<TracksState>()

    private var latestSearchText: String? = null
    fun getStateLiveData(): LiveData<TracksState> = mediatorStateLiveData

    private val mediatorStateLiveData = MediatorLiveData<TracksState>().also { liveData ->
        liveData.addSource(stateLiveData) { state ->
            liveData.value = when (state) {
                is TracksState.Content -> TracksState.Content(state.tracks)
                is TracksState.Empty -> state
                is TracksState.Error -> state
                is TracksState.Loading -> state
            }
        }
    }

    private val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true,
        false
    ) { changedText ->
        searchTracks(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun searchTracks(searchNameTracks: String) {
        if (searchNameTracks.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(searchNameTracks)
                    .collect{ pair -> searchTracksResult(pair.first, pair.second)}
            }
        }
    }

    private fun searchTracksResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                renderState(TracksState.Error(errorMessage = errorMessage,))
            }
            tracks.isEmpty() -> {
                renderState(TracksState.Empty)
            }
            else -> {
                renderState(TracksState.Content(tracks = tracks))
            }
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }
}