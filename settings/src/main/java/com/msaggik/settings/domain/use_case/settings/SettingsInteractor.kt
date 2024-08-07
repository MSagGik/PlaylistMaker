package com.msaggik.settings.domain.use_case.settings

import com.msaggik.settings.domain.model.ThemeSettings

interface SettingsInteractor {

    fun getThemeSettings() : ThemeSettings
    fun getThemeSettingsConsumer(consumer: ThemeConsumer)
    fun updateThemeSetting(themeSettings: ThemeSettings)

    // Callback между IO и UI потоками
    interface ThemeConsumer {
        fun consume(themeSettings: ThemeSettings)
    }
}