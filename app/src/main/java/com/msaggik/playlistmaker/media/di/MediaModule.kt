package com.msaggik.playlistmaker.media.di

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.msaggik.playlistmaker.media.data.favorite_tracks.FavoriteTracks
import com.msaggik.playlistmaker.media.data.favorite_tracks.impl.FavoriteTracksImpl
import com.msaggik.playlistmaker.media.data.repository_impl.MediaRepositoryImpl
import com.msaggik.playlistmaker.media.domain.api.MediaInteractor
import com.msaggik.playlistmaker.media.domain.api.impl.MediaInteractorImpl
import com.msaggik.playlistmaker.media.domain.repository.MediaRepository
import com.msaggik.playlistmaker.media.view_model.FavoriteTracksViewModel
import com.msaggik.playlistmaker.media.view_model.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val TRACK_LIST_PREFERENCES = "track_list_preferences"

val mediaModule = module {

    // view-model
    viewModel{
        FavoriteTracksViewModel(
            mediaInteractor = get(),
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
    single<MediaRepository> {
        MediaRepositoryImpl(
            favoriteTracks = get()
        )
    }

    single<FavoriteTracks> {
        FavoriteTracksImpl(
            spFavoriteTracks = get(),
            gson = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    single {
        Gson()
    }
}