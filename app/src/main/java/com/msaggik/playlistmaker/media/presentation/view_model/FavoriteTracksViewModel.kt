package com.msaggik.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.presentation.view_model.state.FavoriteTracksState

class FavoriteTracksViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private val trackListMediaLiveData = MutableLiveData<FavoriteTracksState>()
    fun getTrackListMediaLiveData(): LiveData<FavoriteTracksState> = trackListMediaLiveData

    init {
        readTrackListMedia()
    }

    private fun readTrackListMedia() {
        mediaInteractor.readTrackListMedia(object : MediaInteractor.MediaConsumer {
            override fun consume(listTracks: List<Track>) {
                if (listTracks.isNullOrEmpty()) {
                    trackListMediaLiveData.postValue(FavoriteTracksState.Empty())
                } else {
                    trackListMediaLiveData.postValue(FavoriteTracksState.Content(listTracks))
                }
            }
        })
    }
}