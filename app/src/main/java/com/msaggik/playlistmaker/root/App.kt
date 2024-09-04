package com.msaggik.playlistmaker.root

import android.app.Application
import com.msaggik.playlistmaker.playlist_manager.di.playlistManagerModule
import com.msaggik.playlistmaker.media.di.mediaModule
import com.msaggik.playlistmaker.player.di.playerModule
import com.msaggik.playlistmaker.playlist.di.playlistModule
import com.msaggik.playlistmaker.search.di.searchModule
import com.msaggik.playlistmaker.setting.di.settingModule
import com.msaggik.playlistmaker.sharing.di.sharingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                playerModule,
                searchModule,
                settingModule,
                sharingModule,
                mediaModule,
                playlistManagerModule,
                playlistModule
            )
        }
    }
}