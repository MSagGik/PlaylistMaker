package com.msaggik.data.api.sp.settings.impl

import android.content.SharedPreferences
import com.msaggik.data.api.sp.settings.ThemeSp

private const val APP_THEME_KEY = "app_theme_key"
class ThemeSpImpl(
    private val spTheme: SharedPreferences
) : ThemeSp {
    override fun isDarkThemeSharedPreferences(): Boolean {
        return spTheme.getBoolean(APP_THEME_KEY, true)
    }

    override fun updateThemeSettingSharedPreferences(isDarkTheme: Boolean) {
        spTheme.edit().putBoolean(APP_THEME_KEY, isDarkTheme).apply()
    }
}