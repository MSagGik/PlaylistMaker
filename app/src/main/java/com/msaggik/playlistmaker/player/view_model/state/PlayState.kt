package com.msaggik.playlistmaker.player.view_model.state

import com.msaggik.playlistmaker.R

sealed class PlayState(val stateViewButton: Int) {
    object Play : PlayState(R.drawable.ic_play)
    object Pause : PlayState(R.drawable.ic_pause)
}