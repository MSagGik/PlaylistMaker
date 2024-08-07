package com.msaggik.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.media.domain.use_case.MediaInteractor
import com.msaggik.media.domain.models.Track
import com.msaggik.playlistmaker.media.presentation.view_model.state.FavoriteTracksState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private val favoriteTrackListLiveData = MutableLiveData<FavoriteTracksState>()
    fun getFavoriteTrackListLiveData(): LiveData<FavoriteTracksState> = favoriteTrackListLiveData

    fun getFavoriteTrackList() {
        viewModelScope.launch {
            mediaInteractor
                .getFavoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    fun addTrackListHistory(track: Track) {
        viewModelScope.launch(Dispatchers.IO){
            mediaInteractor
                .addFavoriteTrack(track)
        }
        getFavoriteTrackList()
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