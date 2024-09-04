package com.msaggik.playlistmaker.setting.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msaggik.playlistmaker.setting.domain.use_case.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings
import com.msaggik.playlistmaker.sharing.domain.use_case.SharingInteractor
import com.msaggik.playlistmaker.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel (
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    // setting theme
    private val mutableSettingsTheme = MutableLiveData<Boolean>()
    private var isDarkTheme = false
    var isSetModifyThemeApp: Boolean? = null

    fun initTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            isDarkTheme = settingsInteractor.getThemeSettings().isDarkTheme
            mutableSettingsTheme.postValue(isDarkTheme)
            viewModelScope.launch(Dispatchers.Main) {
                Utils.setApplicationTheme(isDarkTheme)
            }
        }
    }

    fun getSettingsTheme(): LiveData<Boolean> = mutableSettingsTheme

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.updateThemeSetting(ThemeSettings((isDarkTheme)))
            mutableSettingsTheme.postValue(isDarkTheme)
            viewModelScope.launch(Dispatchers.Main) {
                Utils.setApplicationTheme(isDarkTheme)
            }
        }
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