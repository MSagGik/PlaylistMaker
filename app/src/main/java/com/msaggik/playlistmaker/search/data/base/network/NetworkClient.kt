package com.msaggik.playlistmaker.search.data.base.network

import com.msaggik.playlistmaker.search.data.dto.response.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}