package com.msaggik.playlistmaker.search.data.base.network.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.msaggik.playlistmaker.search.data.dto.request.TracksSearchRequest
import com.msaggik.playlistmaker.search.data.dto.response.Response
import com.msaggik.playlistmaker.search.data.base.network.NetworkClient

private const val ITUNES_BASE_URL = "https://itunes.apple.com"
class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val retrofit = RestItunes.createRetrofitObject(ITUNES_BASE_URL)
    private val itunesRestService = retrofit.create(RestItunes::class.java)
    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        return if (dto is TracksSearchRequest) {
            val response = itunesRestService.search(dto.searchTracks).execute() // get object Response<TrackResponse>, class Response is Retrofit

            val body = response.body() ?: Response() // get object TrackResponse

            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}