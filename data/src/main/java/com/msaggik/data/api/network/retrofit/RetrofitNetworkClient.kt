package com.msaggik.data.api.network.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.msaggik.data.api.network.NetworkClient
import com.msaggik.data.api.network.dto.request.TracksSearchRequest
import com.msaggik.data.api.network.dto.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class RetrofitNetworkClient(
    private val context: Context,
    retrofit: Retrofit
) : NetworkClient {
    private val itunesRestService = retrofit.create(RestItunes::class.java)
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            runCatching {
                itunesRestService.search(dto.searchTracks)
            }.fold(
                onSuccess = { response ->
                    response.apply { resultCode = 200 }
                },
                onFailure = { e ->
                    Response().apply { resultCode = 500 }
                }
            )
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