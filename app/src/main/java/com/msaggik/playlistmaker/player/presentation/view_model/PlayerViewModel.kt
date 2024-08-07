package com.msaggik.playlistmaker.player.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.media.data.converters.TrackDbConverter
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.player.presentation.view_model.state.FavoriteState
import com.msaggik.playlistmaker.player.presentation.view_model.state.PlayState
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: PlayerInteractor,
    private val mediaInteractor: MediaInteractor,
    private val converter: TrackDbConverter
) : ViewModel() {
    companion object {
        private const val PLAYER_DELAY_UPDATE_TRACK_LIST = 250L
    }

    private var timerJob: Job? = null

    private var playerTrackLiveData = MutableLiveData<Track>()
    fun getTrackLiveData(): LiveData<Track> = playerTrackLiveData

    private var buttonStateLiveData = MutableLiveData<PlayState>()
    fun getButtonStateLiveData(): LiveData<PlayState> = buttonStateLiveData

    private var isReverse: Boolean = false
    private var currentTimePlayingLiveData = MutableLiveData<String>()
    fun getCurrentTimePlayingLiveData(): LiveData<String> {
        return currentTimePlayingLiveData
    }

    private var likeStateLiveData = MutableLiveData<FavoriteState>()
    fun getLikeStateLiveData(): LiveData<FavoriteState> = likeStateLiveData

    fun onFavorite(track: Track) {
        if (track.isFavorite) {
            likeStateLiveData.postValue(FavoriteState.Favorite)
        } else {
            likeStateLiveData.postValue(FavoriteState.NotFavorite)
        }
    }

    fun updateFavoriteStatusTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaInteractor
                .getFavoriteTracksId()
                .collect { listFavoriteIdTracks ->
                    updateFavoriteStatus(
                        listFavoriteIdTracks,
                        track
                    )
                }
        }
    }

    fun updateFavoriteStatus(listId: List<Long>, track: Track) {
        track.isFavorite = (listId.isNotEmpty() && listId.contains(track.trackId.toLong()))
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorite) {
                val isDeleted = mediaInteractor.deleteFavoriteTrack(converter.mapSearchToMedia(track))
                if (isDeleted != -1) {
                    track.isFavorite = false
                    likeStateLiveData.postValue(FavoriteState.NotFavorite)
                }
            } else {
                val idFavoriteTrack = mediaInteractor
                    .addFavoriteTrack(
                        converter.mapSearchToMedia(track)
                            .apply { dateAddTrack = System.currentTimeMillis() }
                    )
                if (idFavoriteTrack != -1L) {
                    track.isFavorite = true
                    likeStateLiveData.postValue(FavoriteState.Favorite)
                }
            }
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

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        timerJob?.cancel()
    }

    fun loadingTrack() {
        val track = playerInteractor.loading(trackId = trackId)
        playerTrackLiveData.postValue(track)
    }

    private fun startPlayer() {
        playerInteractor.play()
        buttonStateLiveData.postValue(PlayState.Pause)

        timerJob = viewModelScope.launch {
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

    fun updateTimePlayingLiveData(isReverse: Boolean) {
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

    fun checkPlayPause() {
        when (playerInteractor.getPlayerState()) {
            PlayerState.PLAYER_STATE_PLAYING -> pausePlayer()
            PlayerState.PLAYER_STATE_PREPARED, PlayerState.PLAYER_STATE_PAUSED -> startPlayer()
            else -> {}
        }
    }

    fun releasePlayer() {
        playerInteractor.release()
        buttonStateLiveData.postValue(PlayState.Play)
        timerJob?.cancel()
    }
}