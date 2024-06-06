package com.msaggik.playlistmaker.setting.data.repository

import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings

interface SettingRepository { // интерфейс для связи data - domain
    fun getThemeSettings() : ThemeSettings
    fun updateThemeSetting(themeSettings : ThemeSettings)
}