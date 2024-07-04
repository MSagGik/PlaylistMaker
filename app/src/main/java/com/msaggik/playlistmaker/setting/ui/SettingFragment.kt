package com.msaggik.playlistmaker.setting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentSettingBinding
import com.msaggik.playlistmaker.setting.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.getSettingsTheme().value?.let { binding.switchTheme.setChecked(it) } // start activity

        binding.switchTheme.setOnCheckedChangeListener { switcher, checked -> // checked theme
            settingsViewModel.switchTheme(checked) }

        binding.buttonShare.setOnClickListener(listener)
        binding.buttonSupport.setOnClickListener(listener)
        binding.buttonAgreement.setOnClickListener(listener)
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        override fun onClick(p0: View?) {
            when(p0?.id) {
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