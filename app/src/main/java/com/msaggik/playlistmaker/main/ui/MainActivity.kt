package com.msaggik.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.media.ui.MediaActivity
import com.msaggik.playlistmaker.setting.ui.SettingActivity
import com.msaggik.playlistmaker.search.ui.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSetting = findViewById<Button>(R.id.button_setting)

        buttonSearch.setOnClickListener(listener)
        buttonMedia.setOnClickListener(listener)
        buttonSetting.setOnClickListener(listener)
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        override fun onClick(p0: View?) {
            val intent = when(p0?.id) {
                R.id.button_search -> {
                    Intent(this@MainActivity, SearchActivity::class.java)
                }
                R.id.button_media -> {
                    Intent(this@MainActivity, MediaActivity::class.java)
                }
                R.id.button_setting -> {
                    Intent(this@MainActivity, SettingActivity::class.java)
                }
                else -> {
                    null
                }
            }
            if(intent != null) {
                startActivity(intent)
            }
        }
    }
}
