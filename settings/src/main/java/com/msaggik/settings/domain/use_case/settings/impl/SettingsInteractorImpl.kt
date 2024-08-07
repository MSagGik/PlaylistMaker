package com.msaggik.settings.domain.use_case.settings.impl

import com.msaggik.settings.domain.repository.SettingRepository
import com.msaggik.settings.domain.use_case.settings.SettingsInteractor
import com.msaggik.settings.domain.model.ThemeSettings
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