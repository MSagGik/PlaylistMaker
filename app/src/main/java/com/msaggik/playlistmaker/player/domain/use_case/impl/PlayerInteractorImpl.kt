package com.msaggik.playlistmaker.player.domain.use_case.impl

import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.domain.models.Track

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

    override fun loading(trackId: Int) : Track {
        return trackPlayer.loading(trackId)
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