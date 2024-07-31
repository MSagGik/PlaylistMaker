package com.msaggik.playlistmaker.setting.domain.use_case.impl

import com.msaggik.playlistmaker.setting.domain.repository.SettingRepository
import com.msaggik.playlistmaker.setting.domain.use_case.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import java.util.concurrent.Executors

class SettingsInteractorImpl(
    private val repository: SettingRepository
) : SettingsInteractor {

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