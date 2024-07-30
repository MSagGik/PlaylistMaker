package com.msaggik.playlistmaker.search.data.base.network.retrofit

import com.msaggik.playlistmaker.search.data.dto.response.TrackResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RestItunes {

    companion object{
        fun createRetrofitObject(baseUrl: String) : Retrofit {
            return  Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun createRetrofitObjectTest(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResponse
}