package com.msaggik.playlistmaker.player.domain.api

import com.msaggik.playlistmaker.player.domain.state.PlayerState

interface PlayerInteractor {
    fun getPlayerState(): PlayerState
    fun play()
    fun pause()
    fun stop()
    fun getPlayerCurrentPosition(isReverse: Boolean): Int
    fun release()
}