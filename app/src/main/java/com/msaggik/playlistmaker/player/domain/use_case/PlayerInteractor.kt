package com.msaggik.playlistmaker.player.domain.use_case

import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.domain.models.Track

interface PlayerInteractor {
    fun getPlayerState(): PlayerState
    fun play()
    fun pause()
    fun stop()
    fun loading(trackId: Int) : Track
    fun getPlayerCurrentPosition(isReverse: Boolean): Long
    fun reset()
    fun release()
}