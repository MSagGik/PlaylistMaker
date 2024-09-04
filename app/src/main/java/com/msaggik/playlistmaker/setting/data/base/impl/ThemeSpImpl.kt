package com.msaggik.playlistmaker.setting.data.base.impl

import android.content.SharedPreferences
import com.msaggik.playlistmaker.setting.data.base.ThemeSp

private const val APP_THEME_KEY = "app_theme_key"
class ThemeSpImpl(
    private val spTheme: SharedPreferences
) : ThemeSp {
    override suspend fun isDarkThemeSharedPreferences(): Boolean {
        return spTheme.getBoolean(APP_THEME_KEY, true)
    }

    override suspend fun updateThemeSettingSharedPreferences(isDarkTheme: Boolean) {
        spTheme.edit().putBoolean(APP_THEME_KEY, isDarkTheme).apply()
    }
}