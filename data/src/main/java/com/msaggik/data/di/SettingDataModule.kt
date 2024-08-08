package com.msaggik.data.di

import android.app.Application
import com.msaggik.data.api.sp.settings.ThemeSp
import com.msaggik.data.api.sp.settings.impl.ThemeSpImpl
import com.msaggik.data.repository_impl.ExternalNavigatorImpl
import com.msaggik.data.repository_impl.SettingRepositoryImpl
import com.msaggik.settings.domain.repository.ExternalNavigator
import com.msaggik.settings.domain.repository.SettingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val SHARE_PREFERENCES_THEME = "share_preferences_theme"

val settingDataModule = module {

    // data
    single<SettingRepository> {
        SettingRepositoryImpl(
            themeSp = get()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            context = androidContext()
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