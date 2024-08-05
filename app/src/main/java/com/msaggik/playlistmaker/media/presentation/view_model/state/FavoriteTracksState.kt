package com.msaggik.playlistmaker.media.presentation.view_model.state

import com.msaggik.playlistmaker.media.domain.models.Track

interface FavoriteTracksState {

    object Loading : FavoriteTracksState

    data class Content(
        val trackList: List<Track>
    ) : FavoriteTracksState

    object  Empty : FavoriteTracksState
}