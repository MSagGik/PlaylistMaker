package com.msaggik.settings.di

import com.msaggik.settings.domain.use_case.settings.SettingsInteractor
import com.msaggik.settings.domain.use_case.settings.impl.SettingsInteractorImpl
import com.msaggik.settings.domain.use_case.sharing.SharingInteractor
import com.msaggik.settings.domain.use_case.sharing.impl.SharingInteractorImpl
import com.msaggik.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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

    single<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get()
        )
    }
}