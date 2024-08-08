package com.msaggik.search.di

import com.msaggik.search.presentation.view_model.SearchViewModel
import com.msaggik.search.domain.use_case.TracksInteractor
import com.msaggik.search.domain.use_case.impl.TracksInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {

    // view-model
    viewModel{
        SearchViewModel(
            tracksInteractor = get()
        )
    }

    // domain
    single<TracksInteractor> {
        TracksInteractorImpl(
            repository = get()
        )
    }
}