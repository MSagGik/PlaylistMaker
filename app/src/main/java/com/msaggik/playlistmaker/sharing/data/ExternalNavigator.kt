package com.msaggik.playlistmaker.sharing.data

import com.msaggik.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareApp(shareAppLink: String)
    fun openTerms(termsLink: String)
    fun openSupport(emailData: EmailData)
}