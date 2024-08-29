package com.msaggik.playlistmaker.create_playlist.di

import com.msaggik.playlistmaker.create_playlist.data.mappers.PlaylistMapper
import com.msaggik.playlistmaker.create_playlist.data.repository_impl.PlaylistRepositoryImpl
import com.msaggik.playlistmaker.create_playlist.domain.repository.PlaylistRepository
import com.msaggik.playlistmaker.create_playlist.domain.use_case.PlaylistInteractor
import com.msaggik.playlistmaker.create_playlist.domain.use_case.impl.PlaylistInteractorImpl
import com.msaggik.playlistmaker.create_playlist.presentation.view_model.CreatePlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {

    // view-model
    viewModel{
        CreatePlaylistViewModel(
            playlistInteractor = get()
        )
    }

    // domain
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            repository = get()
        )
    }

    // data
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            context = androidContext(),
            dataBase = get(),
            playlistMapper = get()
        )
    }

    single {
        PlaylistMapper()
    }

}