package com.msaggik.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSetting = findViewById<Button>(R.id.button_setting)

        buttonSearch.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        buttonMedia.setOnClickListener(listener)
        buttonSetting.setOnClickListener(listener)
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        override fun onClick(p0: View?) {
            when(p0?.id) {
                R.id.button_media -> {
                    val intent = Intent(this@MainActivity, MediaActivity::class.java)
                    startActivity(intent)
                }
                R.id.button_setting -> {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
