package com.msaggik.playlistmaker.setting.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentSettingBinding
import com.msaggik.playlistmaker.setting.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()

    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.getSettingsTheme().value?.let { binding.switchTheme.setChecked(it) } // start activity

        binding.switchTheme.setOnCheckedChangeListener { switcher, checked -> // checked theme
            settingsViewModel.switchTheme(checked)
        }

        binding.buttonShare.setOnClickListener(listener)
        binding.buttonSupport.setOnClickListener(listener)
        binding.buttonAgreement.setOnClickListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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