package com.msaggik.playlistmaker.playlist.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentPlaylistBinding
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.models.Track
import com.msaggik.playlistmaker.playlist.presentation.ui.adapter.TracksAdapter
import com.msaggik.playlistmaker.playlist.presentation.view_model.PlaylistViewModel
import com.msaggik.playlistmaker.playlist.presentation.view_model.state.PlaylistWithTracksState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    companion object {
        private const val PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle {
            return bundleOf(
                PLAYLIST_ID to playlistId
            )
        }
    }

    // view-model
    private val playlistViewModel: PlaylistViewModel by viewModel()

    // view
    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding get() = _binding!!

    // bottomSheetBehavior
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    // adapter playlists and listener select playlist to add track
    private var tracks: MutableList<Track> = mutableListOf()

    private val tracksAdapter: TracksAdapter by lazy {
        TracksAdapter(
            tracksAdd = tracks
        ) {
            tracksSelection(it)
        }
    }

    private fun tracksSelection(track: Track) {
        //
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get playlist
        playlistViewModel.getPlaylistWithTracks(requireArguments().getLong(PLAYLIST_ID))

        // creating an object of the BottomSheetBehavior class
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.trackList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = tracksAdapter

        // subscribe to state playlist
        playlistViewModel.getPlaylistWithTracksLiveData().observe(viewLifecycleOwner) { playlist ->
            renderPlaylist(playlist)
        }

        // subscribe to state tracks playlist
        playlistViewModel.getTracksLiveData().observe(viewLifecycleOwner) { state ->
            renderTracks(state)
        }

        // set listeners
        binding.buttonBack.setOnClickListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun renderPlaylist(playlist: PlaylistWithTracks) {
        Glide.with(binding.root)
            .load(playlist.playlist.playlistUriAlbum)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform()
            .into(binding.albumPlaylist)

        binding.playlistName.text = playlist.playlist.playlistName

        if (playlist.playlist.playlistDescription.isNullOrEmpty()) {
            binding.playlistDescription.visibility = View.GONE
        } else {
            binding.playlistDescription.visibility = View.VISIBLE
            binding.playlistDescription.text = playlist.playlist.playlistDescription
        }

        var valueTimeOfTracks = 0L
        for(track in playlist.tracks) {
            valueTimeOfTracks += track.trackTimeMillis
        }
        valueTimeOfTracks = if(valueTimeOfTracks % 60_000 > 0.41) {
            valueTimeOfTracks / 60_000 + 1
        } else {
            valueTimeOfTracks / 60_000
        }

        val timeTracks = binding.root.context.resources.getQuantityString(R.plurals.value_time, valueTimeOfTracks.toInt(), valueTimeOfTracks)
        val numbersTracks = binding.root.context.resources.getQuantityString(R.plurals.number_tracks, playlist.tracks.size, playlist.tracks.size)

        binding.playlistMetaData.text = "$timeTracks ${binding.root.context.getString(R.string.inter_punctum)} $numbersTracks"
    }

    private fun renderTracks(state: PlaylistWithTracksState) {
        when (state) {
            is PlaylistWithTracksState.Content -> {
                showTracksPlaylist(state.playlist)
            }
            is PlaylistWithTracksState.Empty -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracksPlaylist(playlist: PlaylistWithTracks) {
        tracks.clear()
        tracks.addAll(playlist.tracks)
        tracksAdapter.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    findNavController().popBackStack()
                }
            }
        }
    }
}