package com.msaggik.playlistmaker.media.view_model.state

import com.msaggik.playlistmaker.media.domain.models.Track

interface FavoriteTracksState {

    data class Content(
        val trackList: List<Track>
    ) : FavoriteTracksState

    class Empty() : FavoriteTracksState
}