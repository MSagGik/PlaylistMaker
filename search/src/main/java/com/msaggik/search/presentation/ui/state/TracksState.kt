package com.msaggik.search.presentation.ui.state

import com.msaggik.search.domain.model.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    object  Empty : TracksState

    object  HistoryTracks : TracksState
}