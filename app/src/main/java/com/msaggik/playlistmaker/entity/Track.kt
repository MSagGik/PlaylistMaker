package com.msaggik.playlistmaker.entity

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackId")val trackId: Int, // Id композиции
    @SerializedName("trackName")val trackName: String, // Название композиции
    @SerializedName("artistName") val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long, // Продолжительность трека
    @SerializedName("artworkUrl100") val artworkUrl100: String // Ссылка на изображение обложки
)
