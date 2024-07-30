package com.msaggik.playlistmaker.search.di

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.msaggik.playlistmaker.search.data.base.network.NetworkClient
import com.msaggik.playlistmaker.search.data.base.network.retrofit.RetrofitNetworkClient
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.data.repository_impl.TracksRepositoryImpl
import com.msaggik.playlistmaker.search.domain.use_case.TracksInteractor
import com.msaggik.playlistmaker.search.domain.use_case.impl.TracksInteractorImpl
import com.msaggik.playlistmaker.search.domain.repository.TracksRepository
import com.msaggik.playlistmaker.search.presentation.view_model.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNES_BASE_URL = "https://itunes.apple.com"
private const val TRACK_LIST_PREFERENCES = "track_list_preferences"

val searchModule = module {

    // view-model
    viewModel{
        SearchViewModel(
            tracksInteractor = get(),
        )
    }

    // domain
    single<TracksInteractor> {
        TracksInteractorImpl(
            repository = get()
        )
    }

    // data
    single<TracksRepository> {
        TracksRepositoryImpl(
            androidContext(),
            networkClient = get(),
            searchHistorySp = get()
        )
    }
    // network
    single<NetworkClient> {
        RetrofitNetworkClient(
            androidContext(),
            retrofit = get()
        )
    }

    single {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //sp
    single<SearchHistorySp> {
        SearchHistorySpImpl(
            spSearchHistory = get(),
            gson = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    single {
        Gson()
    }

}