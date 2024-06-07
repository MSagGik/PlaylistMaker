package com.msaggik.playlistmaker.player.domain.api.impl

import com.msaggik.playlistmaker.player.data.TrackPlayer
import com.msaggik.playlistmaker.player.domain.api.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState

class PlayerInteractorImpl(private val player: TrackPlayer) : PlayerInteractor {
    override fun getPlayerState(): PlayerState {
        return player.playerState
    }

    override fun play() {
        player.onPlay()
    }

    override fun pause() {
        player.onPause()
    }

    override fun stop() {
        player.onStop()
    }

    override fun getPlayerCurrentPosition(isReverse: Boolean): Int {
        return player.getCurrentPosition(isReverse)
    }

    override fun release() {
        player.onRelease()
    }
}