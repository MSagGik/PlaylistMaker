package com.msaggik.playlistmaker.activity

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val SHARE_PREF_NAME = "share_pref_name"
private const val APP_THEME_KEY = "app_theme_key"

class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE)
        darkTheme = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE).getBoolean(APP_THEME_KEY, false)
        setApplicationTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        setApplicationTheme(darkTheme)
        sharedPreferences.edit().putBoolean(APP_THEME_KEY, darkTheme).apply()
    }

    fun getApplicationTheme(): Boolean {
        return darkTheme
    }

    private fun setApplicationTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}