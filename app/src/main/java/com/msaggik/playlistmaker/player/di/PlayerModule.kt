package com.msaggik.playlistmaker.player.di

import android.media.MediaPlayer
import androidx.room.Room
import com.msaggik.playlistmaker.player.data.mappers.PlayerMapper
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db.DatabaseConfig.DATABASE_NAME
import com.msaggik.playlistmaker.player.data.repository_impl.PlayerRepositoryImpl
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.use_case.impl.PlayerInteractorImpl
import com.msaggik.playlistmaker.player.domain.repository.PlayerRepository
import com.msaggik.playlistmaker.player.presentation.view_model.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    // view-model
    viewModel {
        PlayerViewModel(
            playerInteractor = get()
        )
    }

    // domain
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            playerRepository = get(),
        )
    }

    // data
    factory<PlayerRepository> {
        PlayerRepositoryImpl(
            mediaPlayer = get(),
            converters = get(),
            dataBase = get()
        )
    }

    factory {
        MediaPlayer()
    }

    single {
        PlayerMapper()
    }

    // db
    single {
        Room.databaseBuilder(androidContext(), PlaylistTracksDatabase::class.java, DATABASE_NAME).build()
    }
}