package com.msaggik.playlistmaker.create_playlist.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import com.msaggik.playlistmaker.create_playlist.domain.use_case.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val namesPlaylistLiveData = MutableLiveData<List<String>>()
    fun getNamesPlaylistLiveData(): LiveData<List<String>> = namesPlaylistLiveData

    fun getNamesPlaylist() {
        viewModelScope.launch {
            playlistInteractor.namesPlaylist().collect { list ->
                namesPlaylistLiveData.postValue(list)
            }
        }
    }

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO){
            playlistInteractor.addPlaylist(playlist)
        }
    }

    fun addTrackInPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch(Dispatchers.IO){
            playlistInteractor.insertPlaylistAndAddTrackInPlaylist(playlist, track)
        }
    }
}