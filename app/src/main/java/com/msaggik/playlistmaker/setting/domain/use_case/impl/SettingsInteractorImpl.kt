package com.msaggik.playlistmaker.setting.domain.use_case.impl

import com.msaggik.playlistmaker.setting.domain.repository.SettingRepository
import com.msaggik.playlistmaker.setting.domain.use_case.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val repository: SettingRepository
) : SettingsInteractor {

    override suspend fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override suspend fun updateThemeSetting(themeSettings: ThemeSettings) {
        repository.updateThemeSetting(themeSettings)
    }
}