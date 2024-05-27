package com.msaggik.playlistmaker.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track (
    @SerializedName("trackId")
    val trackId: Int, // Id композиции
    @SerializedName("trackName")
    val trackName: String, // Название композиции
    @SerializedName("artistName")
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long, // Продолжительность трека
    @SerializedName("artworkUrl100")
    val artworkUrl100: String, // Ссылка на изображение обложки
    @SerializedName("collectionName")
    val collectionName: String, // Название альбома
    @SerializedName("releaseDate")
    val releaseDate: String, // Год релиза
    @SerializedName("primaryGenreName")
    val primaryGenreName: String, // Жанр трека
    @SerializedName("country")
    val country: String, // Страна исполнителя
    @SerializedName("previewUrl")
    val previewUrl: String // url отрывка трека
) : Serializable
