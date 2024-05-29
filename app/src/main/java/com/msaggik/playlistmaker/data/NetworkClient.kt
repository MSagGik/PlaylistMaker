package com.msaggik.playlistmaker.data

import com.msaggik.playlistmaker.data.dto.response.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}