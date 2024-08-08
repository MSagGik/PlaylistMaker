package com.msaggik.media.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.msaggik.media.databinding.FragmentMediaBinding
import com.msaggik.media.presentation.ui.adapters.MediaPagerAdapter

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle
        )

        tabMediator = TabLayoutMediator(binding.tabLayoutMedia, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(com.msaggik.common_ui.R.string.favorite_tracks)
                1 -> tab.text = getString(com.msaggik.common_ui.R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}