package com.msaggik.playlistmaker.setting.data.repository_impl

import com.msaggik.playlistmaker.setting.data.base.ThemeSp
import com.msaggik.playlistmaker.setting.data.base.impl.ThemeSpImpl
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import com.msaggik.playlistmaker.setting.domain.repository.SettingRepository

class SettingRepositoryImpl (private val themeSp: ThemeSp) : SettingRepository {
    override fun getThemeSettings(): ThemeSettings {
        val isDarkTheme = (themeSp as ThemeSpImpl).isDarkThemeSharedPreferences()
        return ThemeSettings(isDarkTheme)
    }

    override fun updateThemeSetting(themeSettings: ThemeSettings) {
        (themeSp as ThemeSpImpl).updateThemeSettingSharedPreferences(themeSettings.isDarkTheme)
    }
}