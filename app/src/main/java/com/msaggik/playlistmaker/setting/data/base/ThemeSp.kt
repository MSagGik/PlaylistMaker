package com.msaggik.playlistmaker.setting.data.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

private const val SHARE_PREFERENCES_THEME = "share_preferences_theme"
interface ThemeSp {
    companion object{
        fun createObjectSpTheme(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARE_PREFERENCES_THEME, Application.MODE_PRIVATE)
        }
    }

    fun isDarkThemeSharedPreferences() : Boolean
    fun updateThemeSettingSharedPreferences(isDarkTheme : Boolean)
}