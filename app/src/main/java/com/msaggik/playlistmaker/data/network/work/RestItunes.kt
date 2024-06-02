package com.msaggik.playlistmaker.data.network.work

import com.msaggik.playlistmaker.data.dto.response.TrackResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
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
    fun search(@Query("term") text: String): Call<TrackResponse>
}