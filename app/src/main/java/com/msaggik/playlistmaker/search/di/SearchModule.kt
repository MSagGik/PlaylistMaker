package com.msaggik.playlistmaker.search.di

import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.search.data.base.network.NetworkClient
import com.msaggik.playlistmaker.search.data.base.network.retrofit.RetrofitNetworkClient
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.data.repository_impl.network.TracksRepositoryImpl
import com.msaggik.playlistmaker.search.data.repository_impl.sp.SearchHistorySpRepositoryImpl
import com.msaggik.playlistmaker.search.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.search.domain.api.network.impl.TracksInteractorImpl
import com.msaggik.playlistmaker.search.domain.api.sp.SearchHistoryInteractor
import com.msaggik.playlistmaker.search.domain.api.sp.impl.SearchHistoryInteractorImpl
import com.msaggik.playlistmaker.search.domain.repository.network.TracksRepository
import com.msaggik.playlistmaker.search.domain.repository.sp.SearchHistorySpRepository
import com.msaggik.playlistmaker.search.view_model.SearchViewModel
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
            searchHistoryInteractor = get()
        )
    }

    // domain
    // network
    single<TracksInteractor> {
        TracksInteractorImpl(
            repository = get()
        )
    }

    // sp
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(
            repository = get()
        )
    }

    // data
    // network
    single<TracksRepository> {
        TracksRepositoryImpl(
            androidContext(),
            networkClient = get()
        )
    }

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
    single<SearchHistorySpRepository> {
        SearchHistorySpRepositoryImpl(
            searchHistorySp = get()
        )
    }

    single<SearchHistorySp> {
        SearchHistorySpImpl(
            spSearchHistory = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(TRACK_LIST_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }
}