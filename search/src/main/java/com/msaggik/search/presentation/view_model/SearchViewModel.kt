package com.msaggik.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.common_util.debounce
import com.msaggik.search.presentation.ui.state.TracksState
import com.msaggik.search.domain.model.Track
import com.msaggik.search.domain.use_case.TracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    // trackListHistoryLiveData
    private val trackListHistoryLiveData = MutableLiveData<List<Track>>()

    init {
        readTrackListHistory()
    }

    fun readTrackListHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            tracksInteractor
                .readTrackListHistory()
                .collect { listHistoryTracks ->
                    trackListHistoryLiveData.postValue(listHistoryTracks)
                }
        }
    }

    fun addTrackListHistory(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksInteractor
                .addTrackListHistory(track)
                .collect { listHistoryTracks ->
                    trackListHistoryLiveData.postValue(listHistoryTracks)
                }
        }
    }

    fun clearTrackListHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            tracksInteractor.clearTrackListHistory()
        }
        trackListHistoryLiveData.postValue(emptyList())
    }

    fun getTrackListHistoryLiveData(): LiveData<List<Track>> = trackListHistoryLiveData

    // stateLiveData
    private val stateLiveData = MutableLiveData<TracksState>()

    private var latestSearchText: String? = null
    fun getStateLiveData(): LiveData<TracksState> = mediatorStateLiveData

    fun clearSearchAndSetStateLiveData() {
        if (trackListHistoryLiveData.value?.isEmpty() == true) {
            stateLiveData.postValue(TracksState.Content(emptyList()))
        } else {
            stateLiveData.postValue(TracksState.HistoryTracks)
        }
    }

    private val mediatorStateLiveData = MediatorLiveData<TracksState>().also { liveData ->
        liveData.addSource(stateLiveData) { state ->
            liveData.value = when (state) {
                is TracksState.Content -> TracksState.Content(state.tracks)
                is TracksState.Empty -> state
                is TracksState.Error -> state
                is TracksState.Loading -> state
                is TracksState.HistoryTracks -> state
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
                    .collect { pair -> searchTracksResult(pair.first, pair.second) }
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
                renderState(TracksState.Error(errorMessage = errorMessage))
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