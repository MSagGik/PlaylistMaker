package com.msaggik.playlistmaker.player.presentation.view_model.state

import com.msaggik.playlistmaker.R

sealed class FavoriteState(val stateViewButton: Int) {
    object Favorite : FavoriteState(R.drawable.ic_like_true)
    object NotFavorite : FavoriteState(R.drawable.ic_like_false)
}
