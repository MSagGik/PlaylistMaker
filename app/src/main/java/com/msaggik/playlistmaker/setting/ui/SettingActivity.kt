package com.msaggik.playlistmaker.setting.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ActivitySettingBinding
import com.msaggik.playlistmaker.main.ui.MainActivity
import com.msaggik.playlistmaker.setting.view_model.SettingsViewModel

class SettingActivity : AppCompatActivity() {

    private val settingsViewModel by lazy {
        ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]
    }

    private val binding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settingsViewModel.getSettingsTheme().value?.let { binding.switchTheme.setChecked(it) } // start activity

        binding.switchTheme.setOnCheckedChangeListener { switcher, checked -> // checked theme
            settingsViewModel.switchTheme(checked) }

        binding.buttonBack.setOnClickListener(listener)
        binding.buttonShare.setOnClickListener(listener)
        binding.buttonSupport.setOnClickListener(listener)
        binding.buttonAgreement.setOnClickListener(listener)
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        override fun onClick(p0: View?) {
            when(p0?.id) {
                R.id.button_back -> {
                    val backIntent = Intent(this@SettingActivity, MainActivity::class.java)
                    startActivity(backIntent)
                }
                R.id.button_share -> {
                    settingsViewModel.shareApp()
                }
                R.id.button_support -> {
                    settingsViewModel.openSupport()
                }
                R.id.button_agreement -> {
                    settingsViewModel.openTerms()
                }
            }
        }
    }
}