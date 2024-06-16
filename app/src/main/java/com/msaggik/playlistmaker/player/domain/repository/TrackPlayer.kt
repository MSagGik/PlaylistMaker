package com.msaggik.playlistmaker.player.domain.repository

import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.domain.models.Track

interface TrackPlayer {
    var playerState: PlayerState
    fun onPlay()
    fun onPause()
    fun onStop()
    fun loading(trackId: Int) : Track
    fun getCurrentPosition(isReverse: Boolean): Long
    fun onReset()
    fun onRelease()
}