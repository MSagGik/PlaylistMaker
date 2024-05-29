package com.msaggik.playlistmaker.domain

import android.content.Context
import com.msaggik.playlistmaker.data.network.TracksRepositoryImpl
import com.msaggik.playlistmaker.data.network.work.RetrofitNetworkClient
import com.msaggik.playlistmaker.data.sp.SpRepositoryImpl
import com.msaggik.playlistmaker.data.sp.work.SearchHistoryImpl
import com.msaggik.playlistmaker.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.domain.api.network.TracksRepository
import com.msaggik.playlistmaker.domain.api.sp.SpInteractor
import com.msaggik.playlistmaker.domain.api.sp.SpRepository
import com.msaggik.playlistmaker.domain.impl.network.TracksInteractorImpl
import com.msaggik.playlistmaker.domain.impl.sp.SpInteractorImpl

object Creator { // инициализация репозитория и итерактора
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSpRepository(context: Context): SpRepository {
        return SpRepositoryImpl(SearchHistoryImpl(context))
    }

    fun provideSpInteractor(context: Context): SpInteractor {
        return SpInteractorImpl(getSpRepository(context))
    }
}