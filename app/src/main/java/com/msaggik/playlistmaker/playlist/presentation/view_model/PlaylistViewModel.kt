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

    private val playlistWithTracksLiveData = MutableLiveData<PlaylistWithTracks>()
    fun getPlaylistWithTracksLiveData(): LiveData<PlaylistWithTracks> = playlistWithTracksLiveData

    private val tracksLiveData = MutableLiveData<PlaylistWithTracksState>()
    fun getTracksLiveData(): LiveData<PlaylistWithTracksState> = tracksLiveData

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
        playlistWithTracksLiveData.postValue(playlist)
        if (playlist.tracks.isEmpty()) {
            renderState(PlaylistWithTracksState.Empty)
        } else {
            renderState(PlaylistWithTracksState.Content(playlist))
        }
    }

    private fun renderState(state: PlaylistWithTracksState) {
        tracksLiveData.postValue(state)
    }

    private val successRemoveTrackFromPlaylistLiveData = MutableLiveData<Boolean>()
    fun getSuccessRemoveTrackFromPlaylistLiveData(): LiveData<Boolean> = successRemoveTrackFromPlaylistLiveData

    fun defaultSuccessRemoveTrackFromPlaylistLiveData() {
        successRemoveTrackFromPlaylistLiveData.postValue(true)
    }

    fun removeTrackFromPlaylist(idPlaylist: Long, idTrack: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = playlistInteractor.removeTrackFromPlaylist(idPlaylist, idTrack)
            successRemoveTrackFromPlaylistLiveData.postValue(success != -1)
        }
    }

    fun sharePlaylist(infoPlaylist: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.sharePlaylist(infoPlaylist)
        }
    }

    fun removePlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.removePlaylist(playlistId)
        }
    }
}