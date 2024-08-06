package com.msaggik.playlistmaker.player.di

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.msaggik.playlistmaker.player.data.TrackPlayerImpl
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.use_case.impl.PlayerInteractorImpl
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.presentation.view_model.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val TRACK_LIST_PREFERENCES = "track_list_preferences"
val playerModule = module {

    // view-model
    viewModel { (trackId: Int) ->
        PlayerViewModel(
            trackId = trackId,
            playerInteractor = get(),
            mediaInteractor = get()
        )
    }

    // domain
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            trackPlayer = get(),
        )
    }

    // data
    factory<TrackPlayer> {
        TrackPlayerImpl(
            mediaPlayer = get(),
            spSearchHistory = get(),
            gson = get()
        )
    }

    factory {
        MediaPlayer()
    }

    single {
        androidContext()
            .getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    single {
        Gson()
    }
}