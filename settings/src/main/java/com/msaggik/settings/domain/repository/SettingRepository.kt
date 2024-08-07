package com.msaggik.settings.domain.repository

import com.msaggik.settings.domain.model.ThemeSettings

interface SettingRepository {
    fun getThemeSettings() : ThemeSettings
    fun updateThemeSetting(themeSettings : ThemeSettings)
}