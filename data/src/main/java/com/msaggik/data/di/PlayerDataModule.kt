package com.msaggik.data.di

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.msaggik.data.api.network.mappers.PlayerMapper
import com.msaggik.data.repository_impl.TrackPlayerImpl
import com.msaggik.player.domain.repository.TrackPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val TRACK_LIST_PREFERENCES = "track_list_preferences"
val playerDataModule = module {

    // data
    factory<TrackPlayer> {
        TrackPlayerImpl(
            mediaPlayer = get(),
            spSearchHistory = get(),
            converters = get(),
            gson = get(),
            tracksDataBase = get()
        )
    }

    factory {
        MediaPlayer()
    }

    single {
        PlayerMapper()
    }

    single {
        androidContext()
            .getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    single {
        Gson()
    }
}