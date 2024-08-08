package com.msaggik.data.api.network.dto.response.entities

import com.msaggik.data.api.network.dto.response.Response

data class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
