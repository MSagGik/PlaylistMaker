package com.msaggik.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.presentation.view_model.state.PlaylistWithTracksState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private val playlistWithTracksLiveData = MutableLiveData<PlaylistWithTracksState>()
    fun getPlaylistsWithTracksLiveData(): LiveData<PlaylistWithTracksState> = playlistWithTracksLiveData

    fun getPlaylistWithTracks() {
        viewModelScope.launch {
            mediaInteractor
                .playlistsWithTracks()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlist: List<PlaylistWithTracks>) {
        if (playlist.isEmpty()) {
            renderState(PlaylistWithTracksState.Empty)
        } else {
            renderState(PlaylistWithTracksState.Content(playlist))
        }
    }

    private fun renderState(state: PlaylistWithTracksState) {
        playlistWithTracksLiveData.postValue(state)
    }
}