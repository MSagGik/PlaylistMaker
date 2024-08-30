package com.msaggik.playlistmaker.create_playlist.di

import com.msaggik.playlistmaker.create_playlist.data.mappers.CreatePlaylistMapper
import com.msaggik.playlistmaker.create_playlist.data.repository_impl.CreatePlaylistRepositoryImpl
import com.msaggik.playlistmaker.create_playlist.domain.repository.CreatePlaylistRepository
import com.msaggik.playlistmaker.create_playlist.domain.use_case.CreatePlaylistInteractor
import com.msaggik.playlistmaker.create_playlist.domain.use_case.impl.CreatePlaylistInteractorImpl
import com.msaggik.playlistmaker.create_playlist.presentation.view_model.CreatePlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsModule = module {

    // view-model
    viewModel{
        CreatePlaylistViewModel(
            playlistInteractor = get()
        )
    }

    // domain
    single<CreatePlaylistInteractor> {
        CreatePlaylistInteractorImpl(
            repository = get()
        )
    }

    // data
    single<CreatePlaylistRepository> {
        CreatePlaylistRepositoryImpl(
            context = androidContext(),
            dataBase = get(),
            playlistMapper = get()
        )
    }

    single {
        CreatePlaylistMapper()
    }

}