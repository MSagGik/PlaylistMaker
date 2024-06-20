package com.msaggik.playlistmaker.search.data.dto.response

import com.msaggik.playlistmaker.media.data.dto.TrackDto

data class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
