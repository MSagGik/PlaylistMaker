package com.msaggik.playlistmaker.media.presentation.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.presentation.view_model.state.FavoriteTracksState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val context: Context,
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private val favoriteTrackListLiveData = MutableLiveData<FavoriteTracksState>()
    fun getFavoriteTrackListLiveData(): LiveData<FavoriteTracksState> = favoriteTrackListLiveData

    fun getFavoriteTrackList() {
        renderState(FavoriteTracksState.Loading)
        viewModelScope.launch {
            mediaInteractor
                .getFavoriteTracks()
                .collect{ tracks -> processResult(tracks) }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteTracksState.Empty)
        } else {
            renderState(FavoriteTracksState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteTracksState) {
        favoriteTrackListLiveData.postValue(state)
    }
}