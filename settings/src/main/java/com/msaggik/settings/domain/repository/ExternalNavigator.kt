package com.msaggik.settings.domain.repository

import com.msaggik.settings.domain.model.EmailData

interface ExternalNavigator {
    fun shareApp(shareAppLink: String)
    fun openTerms(termsLink: String)
    fun openSupport(emailData: EmailData)
}