package com.msaggik.playlistmaker.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.msaggik.playlistmaker.player.domain.api.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.player.view_model.state.PlayState
import com.msaggik.playlistmaker.root.App
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {
    companion object {
        private const val PLAYER_DELAY_UPDATE_TRACK_LIST = 250L
        private const val START = "0.00"
        private const val END = "00.30"
        fun getViewModelFactory(trackId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = (this[APPLICATION_KEY] as App).providePlayerInteractor(trackId)
                PlayerViewModel(
                    trackId,
                    interactor,
                )
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private var playerTrackLiveData = MutableLiveData<Track>()
    fun getTrackListHistoryLiveData(): LiveData<Track> = playerTrackLiveData

    private var buttonStateLiveData = MutableLiveData<PlayState>()
    fun getButtonStateLiveData(): LiveData<PlayState> = buttonStateLiveData

    private var isReverse: Boolean = false
    private var currentTimePlayingLiveData = MutableLiveData<String>()
    fun getCurrentTimePlayingLiveData(isReverse: Boolean): LiveData<String> {
        this.isReverse = isReverse
        return currentTimePlayingLiveData
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.stop()
        handler.removeCallbacksAndMessages(null)
    }

    private fun startPlayer() {
        buttonStateLiveData.postValue(PlayState.Pause)
        playerInteractor.play()

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (playerInteractor.getPlayerState() == PlayerState.PLAYER_STATE_PREPARED) {
                    currentTimePlayingLiveData.postValue(if(isReverse) END else START)
                    buttonStateLiveData.postValue(PlayState.Play)
                    handler.removeCallbacksAndMessages(null)
                } else {
                    currentTimePlayingLiveData.postValue(Utils.dateFormatMillisToMinSecShort(playerInteractor.getPlayerCurrentPosition(isReverse).toLong()))
                    handler.postDelayed(this, PLAYER_DELAY_UPDATE_TRACK_LIST)
                }
            }
        }, PLAYER_DELAY_UPDATE_TRACK_LIST)
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        buttonStateLiveData.postValue(PlayState.Play)
        handler.removeCallbacksAndMessages(null)
    }

    fun checkPlayPause() {
        when (playerInteractor.getPlayerState()) {
            PlayerState.PLAYER_STATE_PLAYING -> pausePlayer()
            PlayerState.PLAYER_STATE_PREPARED, PlayerState.PLAYER_STATE_PAUSED -> startPlayer()
            else -> {}
        }
    }
}