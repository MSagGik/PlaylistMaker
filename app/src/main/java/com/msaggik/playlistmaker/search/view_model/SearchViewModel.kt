package com.msaggik.playlistmaker.search.view_model

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msaggik.playlistmaker.search.domain.api.TracksInteractor
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.ui.state.TracksState

class SearchViewModel (
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
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

    private val handlerSearchTrack = Handler(Looper.getMainLooper())

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

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText
        handlerSearchTrack.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchTracks(changedText) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handlerSearchTrack.postDelayed(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                SEARCH_DEBOUNCE_DELAY
            )
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handlerSearchTrack.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }
    }

    fun searchTracks(searchNameTracks: String) {
        if (searchNameTracks.isNotEmpty()) {
            renderState(TracksState.Loading)

            tracksInteractor.searchTracks(searchNameTracks, object : TracksInteractor.TracksConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(listTracks: List<Track>?, errorMessage: String?) {
                    val tracks = mutableListOf<Track>()
                    if (listTracks != null) {
                        tracks.addAll(listTracks)
                    }
                    // create TracksState
                    when {
                        errorMessage != null -> {
                            renderState(
                                TracksState.Error(
                                    errorMessage = errorMessage,
                                )
                            )
                        }

                        tracks.isEmpty() -> {
                            renderState(
                                TracksState.Empty
                            )
                        }

                        else -> {
                            renderState(
                                TracksState.Content(
                                    tracks = tracks,
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }
    override fun onCleared() {
        handlerSearchTrack.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}