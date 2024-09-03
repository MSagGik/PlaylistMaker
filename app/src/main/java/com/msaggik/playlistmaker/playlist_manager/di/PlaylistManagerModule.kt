package com.msaggik.playlistmaker.playlist_manager.di

import com.msaggik.playlistmaker.playlist_manager.data.repository_impl.PlaylistManagerRepositoryImpl
import com.msaggik.playlistmaker.playlist_manager.domain.repository.PlaylistManagerRepository
import com.msaggik.playlistmaker.playlist_manager.domain.use_case.PlaylistManagerInteractor
import com.msaggik.playlistmaker.playlist_manager.domain.use_case.impl.PlaylistManagerInteractorImpl
import com.msaggik.playlistmaker.playlist_manager.presentation.view_model.PlaylistManagerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistManagerModule = module {

    // view-model
    viewModel{
        PlaylistManagerViewModel(
            playlistInteractor = get()
        )
    }

    // domain
    single<PlaylistManagerInteractor> {
        PlaylistManagerInteractorImpl(
            repository = get()
        )
    }

    // data
    single<PlaylistManagerRepository> {
        PlaylistManagerRepositoryImpl(
            context = androidContext(),
            dataBase = get()
        )
    }
}