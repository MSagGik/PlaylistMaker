package com.msaggik.media.di

import com.msaggik.media.domain.use_case.MediaInteractor
import com.msaggik.media.domain.use_case.impl.MediaInteractorImpl
import com.msaggik.media.presentation.view_model.FavoriteTracksViewModel
import com.msaggik.playlistmaker.media.presentation.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    // view-model
    viewModel{
        FavoriteTracksViewModel(
            mediaInteractor = get()
        )
    }

    viewModel{
        PlaylistsViewModel()
    }

    // domain
    single<MediaInteractor> {
        MediaInteractorImpl(
            repository = get()
        )
    }
}