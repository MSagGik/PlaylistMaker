package com.msaggik.playlistmaker.setting.domain.api

import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings

interface SettingsInteractor { // интерфейс для связи domain - view-model

    fun getThemeSettings() : ThemeSettings
    fun getThemeSettingsConsumer(consumer: ThemeConsumer)
    fun updateThemeSetting(themeSettings: ThemeSettings)

    // Callback между IO и UI потоками
    interface ThemeConsumer {
        fun consume(themeSettings: ThemeSettings)
    }
}