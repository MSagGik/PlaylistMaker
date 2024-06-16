package com.msaggik.playlistmaker.setting.di

import android.app.Application
import com.msaggik.playlistmaker.setting.data.base.ThemeSp
import com.msaggik.playlistmaker.setting.data.base.impl.ThemeSpImpl
import com.msaggik.playlistmaker.setting.data.repository_impl.SettingRepositoryImpl
import com.msaggik.playlistmaker.setting.domain.api.SettingsInteractor
import com.msaggik.playlistmaker.setting.domain.api.impl.SettingsInteractorImpl
import com.msaggik.playlistmaker.setting.domain.repository.SettingRepository
import com.msaggik.playlistmaker.setting.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private const val SHARE_PREFERENCES_THEME = "share_preferences_theme"

val settingModule = module {

    // view-model
    viewModel{
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    // domain
    single<SettingsInteractor> {
        SettingsInteractorImpl(
            repository = get()
        )
    }

    // data
    single<SettingRepository> {
        SettingRepositoryImpl(
            themeSp = get()
        )
    }

    single<ThemeSp> {
        ThemeSpImpl(
            spTheme = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(SHARE_PREFERENCES_THEME, Application.MODE_PRIVATE)
    }
}