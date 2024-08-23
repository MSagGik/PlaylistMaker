package com.msaggik.playlistmaker.media.di

import com.msaggik.playlistmaker.media.data.mappers.MediaMapper
import com.msaggik.playlistmaker.media.data.repository_impl.MediaRepositoryImpl
import com.msaggik.playlistmaker.media.domain.repository.MediaRepository
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.use_case.impl.MediaInteractorImpl
import com.msaggik.playlistmaker.media.presentation.view_model.FavoriteTracksViewModel
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
        PlaylistsViewModel(
            mediaInteractor = get()
        )
    }

    // domain
    single<MediaInteractor> {
        MediaInteractorImpl(
            repository = get()
        )
    }

    // data
    single<MediaRepository> {
        MediaRepositoryImpl(
            dataBase = get(),
            mediaMapper = get()
        )
    }

    single {
        MediaMapper()
    }

}