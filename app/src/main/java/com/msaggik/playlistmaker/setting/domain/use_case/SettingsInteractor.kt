package com.msaggik.playlistmaker.setting.domain.use_case

import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings

interface SettingsInteractor {
    suspend fun getThemeSettings() : ThemeSettings
    suspend fun updateThemeSetting(themeSettings : ThemeSettings)
}