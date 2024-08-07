package com.msaggik.playlistmaker.root

import android.app.Application
import com.msaggik.common_util.Utils
import com.msaggik.data.di.mediaDataModule
import com.msaggik.data.di.playerDataModule
import com.msaggik.data.di.searchDataModule
import com.msaggik.data.di.settingDataModule
import com.msaggik.media.di.mediaModule
import com.msaggik.player.di.playerModule
import com.msaggik.search.di.searchModule
import com.msaggik.settings.di.settingModule
import com.msaggik.settings.domain.model.ThemeSettings
import com.msaggik.settings.domain.use_case.settings.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                mediaDataModule,
                playerDataModule,
                searchDataModule,
                settingDataModule,
                mediaModule,
                playerModule,
                searchModule,
                settingModule
            )
        }

        val provideThemeInteractor: SettingsInteractor by inject()
        provideThemeInteractor.getThemeSettingsConsumer(object : SettingsInteractor.ThemeConsumer {
            override fun consume(themeSettings: ThemeSettings) {
                Utils.setApplicationTheme(themeSettings.isDarkTheme)
            }
        })
    }
}