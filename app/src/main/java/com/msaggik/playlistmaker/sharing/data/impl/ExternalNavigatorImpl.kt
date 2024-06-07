package com.msaggik.playlistmaker.sharing.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.sharing.data.ExternalNavigator
import com.msaggik.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    @SuppressLint("QueryPermissionsNeeded")
    override fun shareApp(shareAppLink: String) {
        val formShareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(formShareIntent, context.getString(R.string.default_user))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if(shareIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(shareIntent)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun openTerms(termsLink: String) {
        val agreementUri: Uri = Uri.parse(termsLink)
        val agreementIntent = Intent(Intent.ACTION_VIEW, agreementUri)
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if(agreementIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(agreementIntent)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
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
        if(supportIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(supportEmail)
        }
    }
}