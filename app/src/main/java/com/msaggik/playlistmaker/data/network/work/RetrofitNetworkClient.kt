package com.msaggik.playlistmaker.data.network.work

import com.msaggik.playlistmaker.data.dto.request.TracksSearchRequest
import com.msaggik.playlistmaker.data.dto.response.Response
import com.msaggik.playlistmaker.data.network.NetworkClient

private const val ITUNES_BASE_URL = "https://itunes.apple.com"
class RetrofitNetworkClient : NetworkClient {

    private val retrofit = RestItunes.createRetrofitObject(ITUNES_BASE_URL)
    private val itunesRestService = retrofit.create(RestItunes::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val response = itunesRestService.search(dto.searchTracks).execute() // экземпляр класса Response<TrackResponse>, где класс Response из Retrofit

            val body = response.body() ?: Response() // получение объекта TrackResponse

            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}