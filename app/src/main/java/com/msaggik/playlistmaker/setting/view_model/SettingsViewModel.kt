package com.msaggik.playlistmaker.setting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.msaggik.playlistmaker.setting.domain.api.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import com.msaggik.playlistmaker.root.App
import com.msaggik.playlistmaker.sharing.domain.api.SharingInteractor
import com.msaggik.playlistmaker.util.Utils

class SettingsViewModel (
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    companion object {
        fun getViewModelFactory() : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val sharingInt = (this[APPLICATION_KEY] as App).provideSharingInteractor()
                val settingsInt= (this[APPLICATION_KEY] as App).provideThemeInteractor()
                SettingsViewModel(
                    sharingInt,
                    settingsInt
                )
            }
        }
    }

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