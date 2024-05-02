package com.msaggik.playlistmaker.activity

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SHARE_PREF_NAME = "share_pref_name"
const val APP_THEME_KEY = "app_theme_123"

class App : Application() {

    var darkTheme = false
    lateinit var sharedPreferences: SharedPreferences

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

    fun setApplicationTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}