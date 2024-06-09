package com.msaggik.playlistmaker.setting.domain.repository

import com.msaggik.playlistmaker.setting.domain.model.ThemeSettings

interface SettingRepository { // интерфейс для связи data - domain
    fun getThemeSettings() : ThemeSettings
    fun updateThemeSetting(themeSettings : ThemeSettings)
}