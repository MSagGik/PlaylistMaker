package com.msaggik.playlistmaker.player.domain.repository

import com.msaggik.playlistmaker.player.domain.state.PlayerState

interface TrackPlayer {
    var playerState: PlayerState
    fun onPlay()
    fun onPause()
    fun onStop()
    fun loading(previewUrl: String)
    fun getCurrentPosition(isReverse: Boolean): Long
    fun onReset()
    fun onRelease()
}