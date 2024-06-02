package com.msaggik.playlistmaker.util

import android.content.Context
import com.msaggik.playlistmaker.data.network.TracksRepositoryImpl
import com.msaggik.playlistmaker.data.network.work.RetrofitNetworkClient
import com.msaggik.playlistmaker.data.sp.SpRepositoryImpl
import com.msaggik.playlistmaker.data.sp.work.ManageSpImpl
import com.msaggik.playlistmaker.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.domain.api.network.TracksRepository
import com.msaggik.playlistmaker.domain.api.sp.SpInteractor
import com.msaggik.playlistmaker.domain.api.sp.SpRepository
import com.msaggik.playlistmaker.domain.impl.network.TracksInteractorImpl
import com.msaggik.playlistmaker.domain.impl.sp.SpInteractorImpl

internal object Creator { // инициализация репозитория и итерактора
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSpInteractor(context: Context): SpInteractor {
        return SpInteractorImpl(getSpRepository(context))
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSpRepository(context: Context): SpRepository {
        return SpRepositoryImpl(ManageSpImpl(context))
    }
}