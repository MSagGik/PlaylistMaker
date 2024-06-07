package com.msaggik.playlistmaker.root

import android.app.Application
import com.msaggik.playlistmaker.search.data.base.network.retrofit.RetrofitNetworkClient
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.data.repository.network.TracksRepository
import com.msaggik.playlistmaker.search.data.repository.network.impl.TracksRepositoryImpl
import com.msaggik.playlistmaker.search.data.repository.sp.SearchHistorySpRepository
import com.msaggik.playlistmaker.search.data.repository.sp.impl.SearchHistorySpRepositoryImpl
import com.msaggik.playlistmaker.search.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.search.domain.api.network.impl.TracksInteractorImpl
import com.msaggik.playlistmaker.search.domain.api.sp.SearchHistoryInteractor
import com.msaggik.playlistmaker.search.domain.api.sp.impl.SearchHistoryInteractorImpl
import com.msaggik.playlistmaker.setting.data.base.impl.ThemeSpImpl
import com.msaggik.playlistmaker.setting.data.repository.SettingRepository
import com.msaggik.playlistmaker.setting.data.repository.impl.SettingRepositoryImpl
import com.msaggik.playlistmaker.setting.domain.api.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.api.impl.SettingsInteractorImpl
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import com.msaggik.playlistmaker.sharing.data.ExternalNavigator
import com.msaggik.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.msaggik.playlistmaker.sharing.domain.api.SharingInteractor
import com.msaggik.playlistmaker.sharing.domain.api.impl.SharingInteractorImpl
import com.msaggik.playlistmaker.util.Utils


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        provideThemeInteractor().getThemeSettingsConsumer(object : SettingsInteractor.ThemeConsumer {
            override fun consume(themeSettings: ThemeSettings) {
                Utils.setApplicationTheme(themeSettings.isDarkTheme)
            }
        })
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    fun provideThemeInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getThemeRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(applicationContext), applicationContext)
    }

    private fun getSearchHistoryRepository(): SearchHistorySpRepository {
        return SearchHistorySpRepositoryImpl(SearchHistorySpImpl(applicationContext))
    }

    private fun getThemeRepository(): SettingRepository {
        return SettingRepositoryImpl(ThemeSpImpl(applicationContext))
    }

    private fun getSharingRepository(): ExternalNavigator {
        return ExternalNavigatorImpl(applicationContext)
    }
}