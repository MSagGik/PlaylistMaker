package com.msaggik.playlistmaker.activity

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val buttonShare = findViewById<ImageView>(R.id.button_share)
        val buttonSupport = findViewById<ImageView>(R.id.button_support)
        val buttonAgreement = findViewById<ImageView>(R.id.button_agreement)

        val listener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(p0: View?) {
                when(p0?.id) {
                    R.id.button_back -> {
                        val backIntent = Intent(this@SettingActivity, MainActivity::class.java)
                        startActivity(backIntent)
                    }
                    R.id.button_share -> {
                        val formShareIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_yandex_course))
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(formShareIntent, getString(R.string.default_user))
                        //if(shareIntent.resolveActivity(packageManager) != null) startActivity(shareIntent)
                        startActivity(shareIntent)
                    }
                    R.id.button_support -> {
                        val supportIntent: Intent = Intent().apply {
                            action = ACTION_SENDTO
                            data = Uri.parse("mailto:")
                            putExtra(EXTRA_EMAIL, arrayOf(getString(R.string.email)))
                            putExtra(EXTRA_SUBJECT, getString(R.string.email_title))
                            putExtra(EXTRA_TEXT, getString(R.string.email_text))
                        }
                        val supportEmail = createChooser(supportIntent, null)
                        //if(supportIntent.resolveActivity(packageManager) != null) startActivity(supportIntent)
                        startActivity(supportEmail)
                    }
                    R.id.button_agreement -> {
                        val agreementUri: Uri = Uri.parse(getString(R.string.uri_agreement))
                        val agreementIntent = Intent(Intent.ACTION_VIEW, agreementUri)
                        //if(agreementIntent.resolveActivity(packageManager) != null) startActivity(agreementIntent)
                        startActivity(agreementIntent)
                    }
                }
            }
        }

        buttonBack.setOnClickListener(listener)
        buttonShare.setOnClickListener(listener)
        buttonSupport.setOnClickListener(listener)
        buttonAgreement.setOnClickListener(listener)
    }
}