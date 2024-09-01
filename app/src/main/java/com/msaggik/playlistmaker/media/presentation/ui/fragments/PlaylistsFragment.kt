package com.msaggik.playlistmaker.media.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.playlist_manager.presentation.ui.PlaylistManagerFragment
import com.msaggik.playlistmaker.playlist_manager.presentation.ui.state.PlaylistManagerState
import com.msaggik.playlistmaker.databinding.FragmentPlaylistsBinding
import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.presentation.ui.adapters.PlaylistWithTracksAdapter
import com.msaggik.playlistmaker.media.presentation.view_model.PlaylistsViewModel
import com.msaggik.playlistmaker.media.presentation.view_model.state.CreatePlaylistWithTracksState
import com.msaggik.playlistmaker.playlist.presentation.ui.PlaylistFragment
import com.msaggik.playlistmaker.util.Utils
import com.msaggik.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        private const val DELAY_CLICK_PLAYLIST = 250L
        private val NUMBER_COLUMN_RECYCLERVIEW_LANDSCAPE = 5
        private val NUMBER_COLUMN_RECYCLERVIEW_PORTRAIT = 2
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var playlistClickDebounce: (PlaylistWithTracks) -> Unit

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private var listPlaylistWithTracks: MutableList<PlaylistWithTracks> = mutableListOf()

    private val playlistWithTracksAdapter: PlaylistWithTracksAdapter by lazy {
        PlaylistWithTracksAdapter(listPlaylistWithTracks) {
            playlistWithTracksSelection(it)
        }
    }

    private fun playlistWithTracksSelection(playlist: PlaylistWithTracks) {
        findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment,
           PlaylistFragment.createArgs(playlist.playlist.playlistId))
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding get() = _binding!!
    private var viewArray: Array<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        viewArray = arrayOf(
            binding.playlists,
            binding.placeholderEmptyPlaylists
        )
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistClickDebounce = debounce<PlaylistWithTracks>(
            DELAY_CLICK_PLAYLIST,
            viewLifecycleOwner.lifecycleScope,
            false,
            true
        ) { playlist -> playlistSelection(playlist) }

        playlistsViewModel.getPlaylistWithTracks()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.playlists.layoutManager = GridLayoutManager(activity, NUMBER_COLUMN_RECYCLERVIEW_LANDSCAPE, LinearLayoutManager.VERTICAL, false)
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.playlists.layoutManager = GridLayoutManager(activity, NUMBER_COLUMN_RECYCLERVIEW_PORTRAIT, LinearLayoutManager.VERTICAL, false)
        }

        binding.playlists.adapter = playlistWithTracksAdapter
        playlistWithTracksAdapter.notifyDataSetChanged()

        binding.buttonNewPlaylist.setOnClickListener(listener)

        playlistsViewModel.getPlaylistsWithTracksLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun playlistSelection(playlist: PlaylistWithTracks) {
        findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist.playlist.playlistId))
    }

    private fun render(state: CreatePlaylistWithTracksState) {
        when (state) {
            is CreatePlaylistWithTracksState.Content -> showPlaylistsWithTracks(state.playlists)
            is CreatePlaylistWithTracksState.Empty -> Utils.visibilityView(viewArray, binding.placeholderEmptyPlaylists)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylistsWithTracks(playlists: List<PlaylistWithTracks>) {
        Utils.visibilityView(viewArray, binding.playlists)
        listPlaylistWithTracks.clear()
        listPlaylistWithTracks.addAll(playlists)
        playlistWithTracksAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewArray = emptyArray()
        viewArray = null
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_new_playlist -> {
                    findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment,
                        PlaylistManagerFragment.createArgs(PlaylistManagerState.EmptyArg))
                }
            }
        }
    }
}