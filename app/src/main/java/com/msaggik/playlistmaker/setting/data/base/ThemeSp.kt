package com.msaggik.playlistmaker.setting.data.base

interface ThemeSp {
    suspend fun isDarkThemeSharedPreferences() : Boolean
    suspend fun updateThemeSettingSharedPreferences(isDarkTheme : Boolean)
}