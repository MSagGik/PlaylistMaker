package com.msaggik.playlistmaker.sharing.domain.use_case

interface SharingInteractor { // интерфейс для связи domain - view-model
    fun shareApp()
    fun openTerms()
    fun openSupport()
}