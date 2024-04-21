package com.msaggik.playlistmaker.util.network

import com.msaggik.playlistmaker.entity.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestItunes {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}