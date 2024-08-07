package com.msaggik.data.di

import androidx.room.Room
import com.msaggik.data.api.db.favorite_tracks.TracksDatabase
import com.msaggik.data.api.db.favorite_tracks.entity.config_db.FavoriteTracksConfig.DATABASE_NAME
import com.msaggik.data.api.network.mappers.FavoriteTrackMapper
import com.msaggik.data.repository_impl.FavoriteTracksRepositoryImpl
import com.msaggik.media.domain.repository.FavoriteTracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaDataModule = module {

    // data
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            tracksDataBase = get(),
            favoriteTrackMapper = get()
        )
    }

    single {
        FavoriteTrackMapper()
    }

    // db
    single {
        Room.databaseBuilder(androidContext(), TracksDatabase::class.java, DATABASE_NAME).build()
    }
}