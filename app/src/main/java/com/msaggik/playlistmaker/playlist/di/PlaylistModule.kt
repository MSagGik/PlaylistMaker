package com.msaggik.playlistmaker.playlist.di

import com.msaggik.playlistmaker.playlist.data.mappers.PlaylistMapper
import com.msaggik.playlistmaker.playlist.data.repository_impl.PlaylistRepositoryImpl
import com.msaggik.playlistmaker.playlist.domain.repository.PlaylistRepository
import com.msaggik.playlistmaker.playlist.domain.use_case.PlaylistInteractor
import com.msaggik.playlistmaker.playlist.domain.use_case.impl.PlaylistInteractorImpl
import com.msaggik.playlistmaker.playlist.presentation.view_model.PlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {
    // view-model
    viewModel{
        PlaylistViewModel(
            playlistInteractor = get()
        )
    }

    // domain
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            playlistRepository = get()
        )
    }

    // data
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            dataBase = get(),
            playlistMapper = get()
        )
    }

    single {
        PlaylistMapper()
    }
}