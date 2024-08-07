package com.msaggik.data.di

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.msaggik.data.api.network.NetworkClient
import com.msaggik.data.api.network.mappers.SearchMapper
import com.msaggik.data.api.network.retrofit.RetrofitNetworkClient
import com.msaggik.data.api.sp.search.SearchHistorySp
import com.msaggik.data.api.sp.search.impl.SearchHistorySpImpl
import com.msaggik.data.repository_impl.TracksRepositoryImpl
import com.msaggik.search.domain.repository.TracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNES_BASE_URL = "https://itunes.apple.com"
private const val TRACK_LIST_PREFERENCES = "track_list_preferences"

val searchDataModule = module {

// data
    single<TracksRepository> {
        TracksRepositoryImpl(
            androidContext(),
            networkClient = get(),
            searchHistorySp = get(),
            tracksDatabase = get(),
            converters = get()
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

    single {
        SearchMapper()
    }
}