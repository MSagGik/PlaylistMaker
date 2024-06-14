package com.msaggik.playlistmaker.root

import android.app.Application
import com.msaggik.playlistmaker.player.di.playerModule
import com.msaggik.playlistmaker.search.di.searchModule
import com.msaggik.playlistmaker.setting.data.base.impl.ThemeSpImpl
import com.msaggik.playlistmaker.setting.domain.repository.SettingRepository
import com.msaggik.playlistmaker.setting.data.repository_impl.SettingRepositoryImpl
import com.msaggik.playlistmaker.setting.domain.api.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.api.impl.SettingsInteractorImpl
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import com.msaggik.playlistmaker.sharing.domain.repository.ExternalNavigator
import com.msaggik.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.msaggik.playlistmaker.sharing.domain.api.SharingInteractor
import com.msaggik.playlistmaker.sharing.domain.api.impl.SharingInteractorImpl
import com.msaggik.playlistmaker.util.Utils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        provideThemeInteractor().getThemeSettingsConsumer(object : SettingsInteractor.ThemeConsumer {
            override fun consume(themeSettings: ThemeSettings) {
                Utils.setApplicationTheme(themeSettings.isDarkTheme)
            }
        })

        startKoin{
            androidContext(this@App)
            modules(
                playerModule,
                searchModule,
            )
        }
    }

    fun provideThemeInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getThemeRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository())
    }

    private fun getThemeRepository(): SettingRepository {
        return SettingRepositoryImpl(ThemeSpImpl(applicationContext))
    }

    private fun getSharingRepository(): ExternalNavigator {
        return ExternalNavigatorImpl(applicationContext)
    }
}