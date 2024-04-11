package com.msaggik.playlistmaker

import android.content.Intent
import android.os.Bundle
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

        buttonBack.setOnClickListener {
            val intent = Intent(this@SettingActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}