package com.msaggik.playlistmaker.sharing.di

import com.msaggik.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.msaggik.playlistmaker.sharing.domain.use_case.SharingInteractor
import com.msaggik.playlistmaker.sharing.domain.use_case.impl.SharingInteractorImpl
import com.msaggik.playlistmaker.sharing.domain.repository.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module {

    // domain
    single<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get()
        )
    }

    // data
    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            context = androidContext()
        )
    }
}