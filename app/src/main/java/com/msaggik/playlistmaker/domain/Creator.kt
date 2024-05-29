package com.msaggik.playlistmaker.domain

import com.msaggik.playlistmaker.data.TracksRepositoryImpl
import com.msaggik.playlistmaker.data.network.RetrofitNetworkClient
import com.msaggik.playlistmaker.domain.api.TracksInteractor
import com.msaggik.playlistmaker.domain.api.TracksRepository
import com.msaggik.playlistmaker.domain.impl.TracksInteractorImpl

object Creator { // инициализация репозитория и итерактора
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}