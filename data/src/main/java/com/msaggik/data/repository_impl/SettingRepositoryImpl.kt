package com.msaggik.data.repository_impl

import com.msaggik.data.api.sp.settings.ThemeSp
import com.msaggik.data.api.sp.settings.impl.ThemeSpImpl
import com.msaggik.settings.domain.model.ThemeSettings
import com.msaggik.settings.domain.repository.SettingRepository

class SettingRepositoryImpl (
    private val themeSp: ThemeSp
) : SettingRepository {
    override fun getThemeSettings(): ThemeSettings {
        val isDarkTheme = (themeSp as ThemeSpImpl).isDarkThemeSharedPreferences()
        return ThemeSettings(isDarkTheme)
    }

    override fun updateThemeSetting(themeSettings: ThemeSettings) {
        (themeSp as ThemeSpImpl).updateThemeSettingSharedPreferences(themeSettings.isDarkTheme)
    }
}