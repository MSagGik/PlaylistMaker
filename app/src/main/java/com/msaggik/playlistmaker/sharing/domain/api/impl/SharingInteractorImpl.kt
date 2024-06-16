package com.msaggik.playlistmaker.sharing.domain.api.impl

import com.msaggik.playlistmaker.sharing.domain.repository.ExternalNavigator
import com.msaggik.playlistmaker.sharing.domain.api.SharingInteractor
import com.msaggik.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareApp(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openTerms(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openSupport(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return SHARE_URL
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = EMAIL,
            emailTitle = EMAIL_TITLE,
            emailText = EMAIL_TEXT
        )
    }

    private fun getTermsLink(): String {
        return TERMS_URL
    }

    private companion object{
        private val EMAIL = "example@example.example"
        private val EMAIL_TITLE = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private val EMAIL_TEXT = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private val SHARE_URL = "https://practicum.yandex.ru/catalog/programming/"
        private val TERMS_URL = "https://yandex.ru/legal/practicum_offer/"
    }
}