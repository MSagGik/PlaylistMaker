package com.msaggik.playlistmaker.setting.data.base.impl

import android.content.Context
import com.msaggik.playlistmaker.setting.data.base.ThemeSp

private const val APP_THEME_KEY = "app_theme_key"
class ThemeSpImpl (context: Context) : ThemeSp {

    private val spTheme = ThemeSp.createObjectSpTheme(context)
    override fun isDarkThemeSharedPreferences(): Boolean {
        return spTheme.getBoolean(APP_THEME_KEY, false)
    }

    override fun updateThemeSettingSharedPreferences(isDarkTheme: Boolean) {
        spTheme.edit().putBoolean(APP_THEME_KEY, isDarkTheme).apply()
    }
}