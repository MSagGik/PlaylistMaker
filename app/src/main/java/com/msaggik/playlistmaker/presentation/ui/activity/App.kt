package com.msaggik.playlistmaker.presentation.ui.activity

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.msaggik.playlistmaker.util.Creator
import com.msaggik.playlistmaker.domain.api.sp.SpInteractor


class App : Application() {

    private var darkTheme = false
    private val spInteractor by lazy {
        Creator.provideSpInteractor(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        readDarkThemeSp()
    }

    private fun readDarkThemeSp(){
        spInteractor.isDarkTheme(object : SpInteractor.SpThemeConsumer {
            override fun consume(isDarkTheme: Boolean) {
                darkTheme = isDarkTheme
                setApplicationTheme(darkTheme)
            }
        })
    }

    private fun setDarkThemeSp(isDarkTheme: Boolean) {
        spInteractor.setDarkTheme(isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        setApplicationTheme(darkTheme)
        setDarkThemeSp(darkTheme)
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