package com.msaggik.playlistmaker.search.data.dto.response

data class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
