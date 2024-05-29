package com.msaggik.playlistmaker.presentation.ui.activity

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.msaggik.playlistmaker.util.AppConstants


class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(AppConstants.SHARE_PREF_NAME, MODE_PRIVATE)
        darkTheme = getSharedPreferences(AppConstants.SHARE_PREF_NAME, MODE_PRIVATE).getBoolean(AppConstants.APP_THEME_KEY, false)
        setApplicationTheme(darkTheme)
    }



    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        setApplicationTheme(darkTheme)
        sharedPreferences.edit().putBoolean(AppConstants.APP_THEME_KEY, darkTheme).apply()
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