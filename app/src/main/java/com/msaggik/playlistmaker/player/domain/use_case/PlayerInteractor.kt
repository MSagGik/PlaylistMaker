package com.msaggik.playlistmaker.player.domain.use_case

import com.msaggik.playlistmaker.player.domain.state.PlayerState

interface PlayerInteractor {
    fun getPlayerState(): PlayerState
    fun play()
    fun pause()
    fun stop()
    fun loading(previewUrl: String)
    fun getPlayerCurrentPosition(isReverse: Boolean): Long
    fun reset()
    fun release()
}