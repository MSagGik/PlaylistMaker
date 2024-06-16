package com.msaggik.playlistmaker.setting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msaggik.playlistmaker.setting.domain.api.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import com.msaggik.playlistmaker.sharing.domain.api.SharingInteractor
import com.msaggik.playlistmaker.util.Utils

class SettingsViewModel (
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    // setting theme
    private val mutableSettingsTheme = MutableLiveData<Boolean>()
    private var isDarkTheme = false

    init {
        isDarkTheme = settingsInteractor.getThemeSettings().isDarkTheme
        Utils.setApplicationTheme(isDarkTheme)
        mutableSettingsTheme.postValue(isDarkTheme)
    }

    fun getSettingsTheme(): LiveData<Boolean> = mutableSettingsTheme

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        settingsInteractor.updateThemeSetting(ThemeSettings((isDarkTheme)))
        mutableSettingsTheme.postValue(isDarkTheme)
        Utils.setApplicationTheme(isDarkTheme)
    }

    // setting sharing
    fun shareApp() {
        sharingInteractor.shareApp()
    }
    fun openSupport() {
        sharingInteractor.openSupport()
    }
    fun openTerms() {
        sharingInteractor.openTerms()
    }
}