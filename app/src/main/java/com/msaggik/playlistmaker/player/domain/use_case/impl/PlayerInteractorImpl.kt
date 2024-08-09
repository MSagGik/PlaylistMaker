package com.msaggik.playlistmaker.player.domain.use_case.impl

import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState

class PlayerInteractorImpl(
    private val trackPlayer: TrackPlayer
) : PlayerInteractor {
    override fun getPlayerState(): PlayerState {
        return trackPlayer.playerState
    }

    override fun play() {
        trackPlayer.onPlay()
    }

    override fun pause() {
        trackPlayer.onPause()
    }

    override fun stop() {
        trackPlayer.onStop()
    }

    override fun loading(previewUrl: String) {
        trackPlayer.loading(previewUrl)
    }

    override fun getPlayerCurrentPosition(isReverse: Boolean): Long {
        return trackPlayer.getCurrentPosition(isReverse)
    }

    override fun reset() {
        trackPlayer.onReset()
    }

    override fun release() {
        trackPlayer.onRelease()
    }
}