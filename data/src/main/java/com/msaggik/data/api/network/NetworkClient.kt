package com.msaggik.data.api.network

import com.msaggik.data.api.network.dto.response.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}