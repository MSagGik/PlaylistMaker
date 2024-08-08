package com.msaggik.player.presentation.view_model.state

sealed class PlayState(val stateViewButton: Int) {
    object Play : PlayState(com.msaggik.common_ui.R.drawable.ic_play)
    object Pause : PlayState(com.msaggik.common_ui.R.drawable.ic_pause)
}