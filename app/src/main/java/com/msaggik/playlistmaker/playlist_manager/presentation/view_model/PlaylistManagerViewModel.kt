package com.msaggik.playlistmaker.playlist_manager.presentation.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track
import com.msaggik.playlistmaker.playlist_manager.domain.use_case.PlaylistManagerInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistManagerViewModel(
    private val playlistInteractor: PlaylistManagerInteractor
) : ViewModel() {

    var stateCreateOrEditPlaylist = 0

    private val namesPlaylistLiveData = MutableLiveData<List<String>>()
    fun getNamesPlaylistLiveData(): LiveData<List<String>> = namesPlaylistLiveData

    fun getNamesPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.namesPlaylist().collect { list ->
                namesPlaylistLiveData.postValue(list)
            }
        }
    }

    private val successAddTrackInPlaylistLiveData = MutableLiveData<Pair<Long, String>>()
    fun getSuccessAddTrackInPlaylistLiveData(): LiveData<Pair<Long, String>> = successAddTrackInPlaylistLiveData

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO){
            playlistInteractor.addPlaylist(playlist)
        }
    }

    fun addTrackInPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch(Dispatchers.IO){
            val idPlaylist = playlistInteractor.insertPlaylistAndAddTrackInPlaylist(playlist, track)
            successAddTrackInPlaylistLiveData.postValue(Pair(idPlaylist, playlist.playlistName))
        }
    }

    fun editTrackInPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO){
            playlistInteractor.insertPlaylist(playlist)
        }
    }

    private val uriImageToPrivateStorageLiveData = MutableLiveData<Uri>()
    fun getUriImageToPrivateStorageLiveData(): LiveData<Uri> = uriImageToPrivateStorageLiveData

    fun setUriImageToPrivateStorageLiveData(uri: Uri) {
        uriImageToPrivateStorageLiveData.postValue(uri)
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO){
            val uriImagePrivateStorage = playlistInteractor.saveImageToPrivateStorage(uri)
            uriImageToPrivateStorageLiveData.postValue(uriImagePrivateStorage)
        }
    }
}