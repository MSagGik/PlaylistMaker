package com.msaggik.playlistmaker.player.di

import android.media.MediaPlayer
import com.msaggik.playlistmaker.player.data.TrackPlayerImpl
import com.msaggik.playlistmaker.player.domain.api.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.api.impl.PlayerInteractorImpl
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    // view-model
    viewModel{
        PlayerViewModel(
            playerInteractor = get()
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
        )
    }

    factory {
        MediaPlayer()
    }
}