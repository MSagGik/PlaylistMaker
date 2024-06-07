package com.msaggik.playlistmaker.player.data

import com.msaggik.playlistmaker.player.domain.state.PlayerState

interface TrackPlayer {
    var playerState: PlayerState
    fun onPlay()
    fun onPause()
    fun onStop()
    fun getCurrentPosition(isReverse: Boolean): Int
    fun onRelease()
}