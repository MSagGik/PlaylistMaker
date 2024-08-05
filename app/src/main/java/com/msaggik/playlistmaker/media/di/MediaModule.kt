package com.msaggik.playlistmaker.media.di

import androidx.room.Room
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.TracksDatabase
import com.msaggik.playlistmaker.media.data.favorite_tracks_db.entity.config_db.FavoriteTracksConfig.DATABASE_NAME
import com.msaggik.playlistmaker.media.data.repository_impl.FavoriteTracksRepositoryImpl
import com.msaggik.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.use_case.impl.MediaInteractorImpl
import com.msaggik.playlistmaker.media.presentation.view_model.FavoriteTracksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    // view-model
    viewModel{
        FavoriteTracksViewModel(
            context = androidContext(),
            mediaInteractor = get(),
        )
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
            tracksDataBase = get()
        )
    }

    // db
    single {
        Room.databaseBuilder(androidContext(), TracksDatabase::class.java, DATABASE_NAME).build()
    }

}