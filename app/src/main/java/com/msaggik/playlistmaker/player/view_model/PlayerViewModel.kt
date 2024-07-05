package com.msaggik.playlistmaker.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msaggik.playlistmaker.player.domain.api.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.player.view_model.state.PlayState
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {
    companion object {
        private const val PLAYER_DELAY_UPDATE_TRACK_LIST = 250L
    }

    private val handler = Handler(Looper.getMainLooper())

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
        handler.removeCallbacksAndMessages(null)
    }

    fun loadingTrack() {
        val track = playerInteractor.loading(trackId = trackId)
        playerTrackLiveData.postValue(track)
    }

    private fun startPlayer() {
        playerInteractor.play()
        buttonStateLiveData.postValue(PlayState.Pause)

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PREPARED) {
                    currentTimePlayingLiveData.postValue(Utils.dateFormatMillisToMinSecShort(playerInteractor.getPlayerCurrentPosition(isReverse)))
                    buttonStateLiveData.postValue(PlayState.Play)
                    handler.removeCallbacksAndMessages(null)
                } else {
                    currentTimePlayingLiveData.postValue(
                        Utils.dateFormatMillisToMinSecShort(
                            playerInteractor.getPlayerCurrentPosition(isReverse)
                        )
                    )
                    handler.postDelayed(this, PLAYER_DELAY_UPDATE_TRACK_LIST)
                }
            }
        }, PLAYER_DELAY_UPDATE_TRACK_LIST)
    }

    fun pausePlayer() {
        if (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PLAYING) {
            playerInteractor.pause()
            buttonStateLiveData.postValue(PlayState.Play)
            handler.removeCallbacksAndMessages(null)
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
        handler.removeCallbacksAndMessages(null)
    }
}