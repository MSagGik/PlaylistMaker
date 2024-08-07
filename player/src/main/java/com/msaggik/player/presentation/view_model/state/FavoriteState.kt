package com.msaggik.player.presentation.view_model.state

sealed class FavoriteState(val stateViewButton: Int) {
    object Favorite : FavoriteState(com.msaggik.common_ui.R.drawable.ic_like_true)
    object NotFavorite : FavoriteState(com.msaggik.common_ui.R.drawable.ic_like_false)
}
