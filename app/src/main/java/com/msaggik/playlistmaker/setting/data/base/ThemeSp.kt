package com.msaggik.playlistmaker.setting.data.base

interface ThemeSp {
    fun isDarkThemeSharedPreferences() : Boolean
    fun updateThemeSettingSharedPreferences(isDarkTheme : Boolean)
}