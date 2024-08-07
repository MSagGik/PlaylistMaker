package com.msaggik.playlistmaker.media.presentation.view_model.state

import com.msaggik.media.domain.models.Track

interface FavoriteTracksState {

    data class Content(
        val trackList: List<Track>
    ) : FavoriteTracksState

    object  Empty : FavoriteTracksState
}