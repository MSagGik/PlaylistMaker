package com.msaggik.playlistmaker.data.network

import com.msaggik.playlistmaker.data.dto.response.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}