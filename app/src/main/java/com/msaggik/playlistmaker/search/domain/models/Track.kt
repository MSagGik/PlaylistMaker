package com.msaggik.playlistmaker.search.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Track (
    val trackId: Int = -1, // Id композиции
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String // url отрывка трека
) : Parcelable, Serializable
