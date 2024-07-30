package com.msaggik.playlistmaker.search.presentation.ui.state

import com.msaggik.playlistmaker.search.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    object  Empty : TracksState
}