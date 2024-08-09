package com.msaggik.data.di

import android.media.MediaPlayer
import com.msaggik.data.api.network.mappers.PlayerMapper
import com.msaggik.data.repository_impl.TrackPlayerImpl
import com.msaggik.player.domain.repository.TrackPlayer
import org.koin.dsl.module

val playerDataModule = module {

    // data
    factory<TrackPlayer> {
        TrackPlayerImpl(
            mediaPlayer = get(),
            converters = get(),
            tracksDataBase = get()
        )
    }

    factory {
        MediaPlayer()
    }

    single {
        PlayerMapper()
    }

}