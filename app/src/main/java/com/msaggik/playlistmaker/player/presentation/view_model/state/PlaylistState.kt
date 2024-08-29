package com.msaggik.playlistmaker.player.presentation.view_model.state

import com.msaggik.playlistmaker.R

sealed class PlaylistState(val stateViewButton: Int) {
    object InPlaylist : PlaylistState(R.drawable.ic_add_true)
    object NoInPlaylist : PlaylistState(R.drawable.ic_add)
}