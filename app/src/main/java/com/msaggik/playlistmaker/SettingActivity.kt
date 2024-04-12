package com.msaggik.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
                        val supportIntent = Intent(Intent.ACTION_SENDTO)
                        supportIntent.data = Uri.parse("mailto:")
                        supportIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email))
                        supportIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.email_title))
                        supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
                        //if(supportIntent.resolveActivity(packageManager) != null) startActivity(supportIntent)
                        startActivity(supportIntent)
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