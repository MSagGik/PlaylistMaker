package com.msaggik.playlistmaker.root

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ActivityRootBinding
import com.msaggik.playlistmaker.setting.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    private val binding: ActivityRootBinding by lazy {
        ActivityRootBinding.inflate(layoutInflater)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment, R.id.createPlaylistFragment, R.id.playlistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.view.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.view.visibility = View.VISIBLE
                }
            }
        }

        initThemeActivity()
    }

    private fun initThemeActivity() {

        val currentNightModeSystem = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val isDarkThemeSystem = when (currentNightModeSystem) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }

        if(settingsViewModel.isSetModifyThemeApp != null && settingsViewModel.isSetModifyThemeApp != isDarkThemeSystem) {
            settingsViewModel.switchTheme(isDarkThemeSystem)
        } else {
            settingsViewModel.initTheme()
        }
        settingsViewModel.isSetModifyThemeApp = isDarkThemeSystem
    }
}