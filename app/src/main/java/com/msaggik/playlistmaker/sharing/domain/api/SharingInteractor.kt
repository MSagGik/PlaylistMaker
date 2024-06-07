package com.msaggik.playlistmaker.sharing.domain.api

interface SharingInteractor { // интерфейс для связи domain - view-model
    fun shareApp()
    fun openTerms()
    fun openSupport()
}