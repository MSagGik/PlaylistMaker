package com.msaggik.player.di

import com.msaggik.player.domain.use_case.PlayerInteractor
import com.msaggik.player.domain.use_case.impl.PlayerInteractorImpl
import com.msaggik.player.presentation.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    // view-model
    viewModel { (trackId: Int) ->
        PlayerViewModel(
            trackId = trackId,
            playerInteractor = get(),
        )
    }

    // domain
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            trackPlayer = get(),
        )
    }
}