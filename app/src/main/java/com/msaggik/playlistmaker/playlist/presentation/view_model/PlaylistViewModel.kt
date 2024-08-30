package com.msaggik.playlistmaker.playlist.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.use_case.PlaylistInteractor
import com.msaggik.playlistmaker.playlist.presentation.view_model.state.PlaylistWithTracksState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val playlistWithTracksLiveData = MutableLiveData<PlaylistWithTracksState>()
    fun getPlaylistWithTracksLiveData(): LiveData<PlaylistWithTracksState> = playlistWithTracksLiveData

    fun getPlaylistWithTracks(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .playlistWithTracks(playlistId)
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    private fun processResult(playlist: PlaylistWithTracks) {
        if (playlist.tracks.isEmpty()) {
            renderState(PlaylistWithTracksState.Empty)
        } else {
            renderState(PlaylistWithTracksState.Content(playlist))
        }
    }

    private fun renderState(state: PlaylistWithTracksState) {
        playlistWithTracksLiveData.postValue(state)
    }
}