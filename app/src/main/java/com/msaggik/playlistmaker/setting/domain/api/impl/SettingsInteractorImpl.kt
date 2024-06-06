package com.msaggik.playlistmaker.setting.domain.api.impl

import com.msaggik.playlistmaker.setting.data.repository.SettingRepository
import com.msaggik.playlistmaker.setting.domain.api.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import java.util.concurrent.Executors

class SettingsInteractorImpl(private val repository: SettingRepository) : SettingsInteractor {

    override fun getThemeSettings() : ThemeSettings {
        return repository.getThemeSettings()
    }

    val executor = Executors.newCachedThreadPool()
    override fun getThemeSettingsConsumer(consumer: SettingsInteractor.ThemeConsumer) {
        executor.execute {
            consumer.consume(repository.getThemeSettings())
        }
    }

    override fun updateThemeSetting(themeSettings: ThemeSettings) {
        executor.execute {
            repository.updateThemeSetting(themeSettings)
        }
    }
}