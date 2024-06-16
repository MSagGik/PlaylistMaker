package com.msaggik.playlistmaker.search.data.dto.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TrackDto (
    @SerializedName("trackId")
    val trackId: Int = -1, // Id композиции
    @SerializedName("trackName")
    val trackName: String = "", // Название композиции
    @SerializedName("artistName")
    val artistName: String = "", // Имя исполнителя
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long = -1L, // Продолжительность трека
    @SerializedName("artworkUrl100")
    val artworkUrl100: String = "", // Ссылка на изображение обложки
    @SerializedName("collectionName")
    val collectionName: String = "", // Название альбома
    @SerializedName("releaseDate")
    val releaseDate: String = "", // Год релиза
    @SerializedName("primaryGenreName")
    val primaryGenreName: String = "", // Жанр трека
    @SerializedName("country")
    val country: String = "", // Страна исполнителя
    @SerializedName("previewUrl")
    val previewUrl: String = "" // url отрывка трека
) : Serializable
