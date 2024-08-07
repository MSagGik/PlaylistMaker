package com.msaggik.data.api.sp.settings

interface ThemeSp {
    fun isDarkThemeSharedPreferences() : Boolean
    fun updateThemeSettingSharedPreferences(isDarkTheme : Boolean)
}