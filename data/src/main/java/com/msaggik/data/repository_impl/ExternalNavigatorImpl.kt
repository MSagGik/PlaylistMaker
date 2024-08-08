package com.msaggik.data.repository_impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.msaggik.settings.domain.model.EmailData
import com.msaggik.settings.domain.repository.ExternalNavigator

class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {

    override fun shareApp(shareAppLink: String) {
        val formShareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(formShareIntent, context.getString(com.msaggik.common_ui.R.string.default_user))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(shareIntent)
    }

    override fun openTerms(termsLink: String) {
        val agreementUri: Uri = Uri.parse(termsLink)
        val agreementIntent = Intent(Intent.ACTION_VIEW, agreementUri)
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(agreementIntent)
    }

    override fun openSupport(emailData: EmailData) {
        val supportIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.emailTitle)
            putExtra(Intent.EXTRA_TEXT, emailData.emailText)
        }
        val supportEmail = Intent.createChooser(supportIntent, null)
        supportEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(supportEmail)
    }
}