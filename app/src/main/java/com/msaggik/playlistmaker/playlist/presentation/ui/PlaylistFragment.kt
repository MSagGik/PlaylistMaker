package com.msaggik.playlistmaker.playlist.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
        playlistViewModel.getPlaylistWithTracksLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun render(state: PlaylistWithTracksState) {
        when (state) {
            is PlaylistWithTracksState.Content -> showTracksPlaylist(state.playlist)
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
}