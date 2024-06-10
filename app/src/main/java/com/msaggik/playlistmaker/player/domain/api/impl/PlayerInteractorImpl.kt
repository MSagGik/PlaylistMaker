package com.msaggik.playlistmaker.player.domain.api.impl

import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.api.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.domain.models.Track

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

    override fun loading(trackId: Int) : Track {
        return player.loading(trackId)
    }

    override fun getPlayerCurrentPosition(isReverse: Boolean): Int {
        return player.getCurrentPosition(isReverse)
    }

    override fun release() {
        player.onRelease()
    }
}