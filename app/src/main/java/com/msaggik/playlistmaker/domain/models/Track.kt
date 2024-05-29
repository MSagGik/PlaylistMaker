package com.msaggik.playlistmaker.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track (
    val trackId: Int, // Id композиции
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String // url отрывка трека
) : Serializable
