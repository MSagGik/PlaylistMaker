package com.msaggik.playlistmaker.player.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.player.presentation.view_model.state.FavoriteState
import com.msaggik.playlistmaker.player.presentation.view_model.state.PlayState
import com.msaggik.playlistmaker.player.presentation.view_model.state.PlaylistState
import com.msaggik.playlistmaker.player.presentation.view_model.state.PlaylistWithTracksState
import com.msaggik.playlistmaker.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    companion object {
        private const val PLAYER_DELAY_UPDATE_TRACK_LIST = 250L
    }

    /**
     * Player ViewModel
     * @param playerTrackLiveData - LiveData state instance entity Track
     * @param buttonStateLiveData - LiveData state button Play/Pause
     * @param currentTimePlayingLiveData - LiveData state track time progress
     * @param isReverse - state reverse track time countdown
     */
    private var timerJob: Job? = null
    var buttonStatePrePlay = false

    // Player state
    private var playerTrackLiveData = MutableLiveData<Track>()
    fun getTrackLiveData(): LiveData<Track> = playerTrackLiveData

    private var buttonStateLiveData = MutableLiveData<PlayState>()
    fun getButtonStateLiveData(): LiveData<PlayState> = buttonStateLiveData

    private var isReverse: Boolean = false
    private var currentTimePlayingLiveData = MutableLiveData<String>()
    fun getCurrentTimePlayingLiveData(): LiveData<String> {
        return currentTimePlayingLiveData
    }

    // Manage player and state modification
    fun loadingTrack(track: Track) {
        if(playerTrackLiveData.value == null && !track.previewUrl.isNullOrEmpty()) {
            playerInteractor.loading(track.previewUrl)
            playerTrackLiveData.postValue(track)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        timerJob?.cancel()
    }

    fun startPlayer() {
        playerInteractor.play()
        buttonStateLiveData.postValue(PlayState.Pause)

        timerJob = viewModelScope.launch (Dispatchers.Default) {
            while (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PLAYING) {
                updateTimePlayingLiveData(isReverse)
                delay(PLAYER_DELAY_UPDATE_TRACK_LIST)
            }
            if (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PREPARED) {
                updateTimePlayingLiveData(isReverse)
                buttonStateLiveData.postValue(PlayState.Play)
                timerJob?.cancel()
            }
        }
    }

    private fun updateTimePlayingLiveData(isReverse: Boolean) {
        currentTimePlayingLiveData.postValue(
            Utils.dateFormatMillisToMinSecShort(
                playerInteractor.getPlayerCurrentPosition(isReverse)
            )
        )
    }

    fun pausePlayer() {
        if (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PLAYING) {
            playerInteractor.pause()
            buttonStateLiveData.postValue(PlayState.Play)
            timerJob?.cancel()
        }
    }

    fun isReverse() {
        isReverse = !isReverse
        currentTimePlayingLiveData.postValue(
            Utils.dateFormatMillisToMinSecShort(
                playerInteractor.getPlayerCurrentPosition(isReverse)
            )
        )
    }

    fun checkPlayPause() {
        buttonStatePrePlay = when (playerInteractor.getPlayerState()) {
            PlayerState.PLAYER_STATE_PLAYING -> {
                pausePlayer()
                false
            }
            PlayerState.PLAYER_STATE_PREPARED, PlayerState.PLAYER_STATE_PAUSED -> {
                startPlayer()
                true
            }
            else -> {
                false
            }
        }
    }

    /**
     * Favorite ViewModel
     * @param likeStateLiveData - LiveData status of track in favorites
     */

    // Favorite state
    private var likeStateLiveData = MutableLiveData<FavoriteState>()
    fun getLikeStateLiveData(): LiveData<FavoriteState> = likeStateLiveData

    // State modification
    fun onFavorite(track: Track) {
        if (track.isFavorite) {
            likeStateLiveData.postValue(FavoriteState.Favorite)
        } else {
            likeStateLiveData.postValue(FavoriteState.NotFavorite)
        }
    }

    fun updateFavoriteStatusTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor
                .getFavoriteTracksId()
                .collect { listFavoriteIdTracks ->
                    updateFavoriteStatus(
                        listFavoriteIdTracks,
                        track
                    )
                }
        }
    }

    private fun updateFavoriteStatus(listId: List<Long>, track: Track) {
        track.isFavorite = (listId.isNotEmpty() && listId.contains(track.trackId.toLong()))
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseDatabase = playerInteractor.setFavoriteTrack(
                track.apply {
                    isFavorite = !track.isFavorite
                    dateAddTrack = System.currentTimeMillis()
                }
            )
            if (responseDatabase != -1L) {
                val state = if(track.isFavorite) FavoriteState.Favorite else FavoriteState.NotFavorite
                likeStateLiveData.postValue(state)
            }
        }
    }

    /**
     * Add track in playlist ViewModel
     * @param playlistStateLiveData - LiveData status of track in playlists
     * @param playlistWithTracksLiveData - LiveData state instance entity PlaylistWithTracksState
     * @param successAddTrackInPlaylistLiveData - LiveData status of successful adding of a track to a playlist
     */

    // Playlist state
    private var playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistStateLiveData(): LiveData<PlaylistState> = playlistStateLiveData

    private val playlistWithTracksLiveData = MutableLiveData<PlaylistWithTracksState>()
    fun getPlaylistsWithTracksLiveData(): LiveData<PlaylistWithTracksState> = playlistWithTracksLiveData

    private val successAddTrackInPlaylistLiveData = MutableLiveData<Boolean>()
    fun getSuccessAddTrackInPlaylistLiveData(): LiveData<Boolean> = successAddTrackInPlaylistLiveData

    // State modification
    fun hasInPlaylist(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val hasTrackInPlaylist = playerInteractor.isTrackInPlaylistAndTrack(track.trackId.toLong())
            if (hasTrackInPlaylist) {
                playlistStateLiveData.postValue(PlaylistState.InPlaylist)
            } else {
                playlistStateLiveData.postValue(PlaylistState.NoInPlaylist)
            }
        }
    }

    fun getPlaylistWithTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor
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

    fun addTrackInPlaylist(idPlaylist: Long, track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val successAddTrackInPlaylist = playerInteractor
                .addTrackInPlaylist(idPlaylist, track)
            successAddTrackInPlaylistLiveData.postValue(successAddTrackInPlaylist != -1L)
            hasInPlaylist(track)
        }
    }
}