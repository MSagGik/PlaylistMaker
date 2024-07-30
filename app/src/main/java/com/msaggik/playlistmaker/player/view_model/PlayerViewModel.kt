package com.msaggik.playlistmaker.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.player.domain.api.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.player.view_model.state.PlayState
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: PlayerInteractor,
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

    fun isReverse() {
        isReverse = !isReverse
        currentTimePlayingLiveData.postValue(Utils.dateFormatMillisToMinSecShort(playerInteractor.getPlayerCurrentPosition(isReverse)))
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
                currentTimePlayingLiveData.postValue(
                    Utils.dateFormatMillisToMinSecShort(
                        playerInteractor.getPlayerCurrentPosition(isReverse)
                    )
                )
                delay(PLAYER_DELAY_UPDATE_TRACK_LIST)
            }
            if (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PREPARED) {
                currentTimePlayingLiveData.postValue(Utils.dateFormatMillisToMinSecShort(playerInteractor.getPlayerCurrentPosition(isReverse)))
                buttonStateLiveData.postValue(PlayState.Play)
                timerJob?.cancel()
            }
        }
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