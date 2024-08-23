package com.msaggik.playlistmaker.media.di

import com.msaggik.playlistmaker.media.data.mappers.FavoriteTrackMapper
import com.msaggik.playlistmaker.media.data.repository_impl.FavoriteTracksRepositoryImpl
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
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
        PlaylistsViewModel()
    }

    // domain
    single<MediaInteractor> {
        MediaInteractorImpl(
            repository = get()
        )
    }

    // data
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            dataBase = get(),
            favoriteTrackMapper = get()
        )
    }

    single {
        FavoriteTrackMapper()
    }

}