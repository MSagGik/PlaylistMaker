package com.msaggik.playlistmaker.creator

import android.content.Context
import com.msaggik.playlistmaker.search.data.repository.network.impl.TracksRepositoryImpl
import com.msaggik.playlistmaker.search.data.base.network.retrofit.RetrofitNetworkClient
import com.msaggik.playlistmaker.search.data.repository.sp.impl.SearchHistorySpRepositoryImpl
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.search.data.repository.network.TracksRepository
import com.msaggik.playlistmaker.search.domain.api.sp.SearchHistoryInteractor
import com.msaggik.playlistmaker.search.data.repository.sp.SearchHistorySpRepository
import com.msaggik.playlistmaker.search.domain.api.network.impl.TracksInteractorImpl
import com.msaggik.playlistmaker.search.domain.api.sp.impl.SearchHistoryInteractorImpl

internal object Creator { // инициализация репозитория и итерактора
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

//    fun provideThemeInteractor(context: Context): SettingsInteractor {
//        return SettingsInteractorImpl(getThemeRepository(context))
//    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistorySpRepository {
        return SearchHistorySpRepositoryImpl(SearchHistorySpImpl(context))
    }

//    private fun getThemeRepository(context: Context): SettingRepository {
//        return SettingRepositoryImpl(ThemeSpImpl(context))
//    }

}