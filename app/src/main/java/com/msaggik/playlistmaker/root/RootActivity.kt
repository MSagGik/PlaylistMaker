package com.msaggik.playlistmaker.root

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

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
                R.id.playerFragment, R.id.createPlaylistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.view.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.view.visibility = View.VISIBLE
                }
            }
        }
    }
}