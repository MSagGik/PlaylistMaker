package com.msaggik.playlistmaker.playlist.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentPlaylistBinding
import com.msaggik.playlistmaker.player.presentation.ui.PlayerFragment
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.models.Track
import com.msaggik.playlistmaker.playlist.presentation.ui.adapter.TracksAdapter
import com.msaggik.playlistmaker.playlist.presentation.view_model.PlaylistViewModel
import com.msaggik.playlistmaker.playlist.presentation.view_model.state.PlaylistWithTracksState
import com.msaggik.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    companion object {
        const val DELAY_CLICK_TRACK = 250L
        private const val PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle {
            return bundleOf(
                PLAYLIST_ID to playlistId
            )
        }
    }

    // debounces
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onTrackLongClickDebounce: (Track) -> Unit

    // view-model
    private val playlistViewModel: PlaylistViewModel by viewModel()

    // view
    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding get() = _binding!!

    // bottomSheetBehavior
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    // adapter playlists and listener select playlist to add track
    private var tracks: MutableList<Track> = mutableListOf()
    private lateinit var selectedTrack: Track

    private val tracksAdapter: TracksAdapter by lazy {
        TracksAdapter(
            tracksAdd = tracks,
            trackClickListener = {
                onTrackClickDebounce(it)
            },
            trackLongClickListener = {
                selectedTrack = it
                deleteTrackDialog.show()
            }
        )
    }

    // parameter for dialog window
    private val deleteTrackDialog: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(requireActivity().getString(R.string.title_delete_track))
            .setMessage(requireActivity().getString(R.string.question_delete_track))
            .setNegativeButton(requireActivity().getString(R.string.cancel)) { dialog, which -> }
            .setPositiveButton(requireActivity().getString(R.string.delete)) { dialog, which ->
                playlistViewModel.removeTrackFromPlaylist(
                    requireArguments().getLong(PLAYLIST_ID),
                    selectedTrack.trackId.toLong()
                )
                playlistViewModel.getPlaylistWithTracks(requireArguments().getLong(PLAYLIST_ID))
            }
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

        playlistViewModel.defaultSuccessRemoveTrackFromPlaylistLiveData()

        initDebounces()

        // get playlist
        playlistViewModel.getPlaylistWithTracks(requireArguments().getLong(PLAYLIST_ID))

        // creating an object of the BottomSheetBehavior class
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.layoutBottomSheet.visibility = View.GONE

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

        // subscribe to state delete track from playlist
        playlistViewModel.getSuccessRemoveTrackFromPlaylistLiveData().observe(viewLifecycleOwner) {
            if(!it) Toast.makeText(requireContext(), requireContext().getString(R.string.failed_to_delete_track), Toast.LENGTH_SHORT).show()
        }

        // set listeners
        binding.buttonBack.setOnClickListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDebounces() {
        onTrackClickDebounce = onTrackClickDebounceAll { track -> trackSelection(track) }
        onTrackLongClickDebounce = onTrackClickDebounceAll { track -> trackLongSelection(track) }
    }

    private fun onTrackClickDebounceAll(action: (Track) -> Unit): (Track) -> Unit {
        return debounce<Track>(
            DELAY_CLICK_TRACK,
            viewLifecycleOwner.lifecycleScope,
            false,
            true,
            action
        )
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

    @SuppressLint("NotifyDataSetChanged")
    private fun renderTracks(state: PlaylistWithTracksState) {
        when (state) {
            is PlaylistWithTracksState.Content -> {
                showTracksPlaylist(state.playlist)
            }
            is PlaylistWithTracksState.Empty -> {
                noShowTracksPlaylist()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracksPlaylist(playlist: PlaylistWithTracks) {
        binding.layoutBottomSheet.visibility = View.VISIBLE
        tracks.clear()
        tracks.addAll(playlist.tracks)
        tracksAdapter.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun noShowTracksPlaylist() {
        tracks.clear()
        tracksAdapter.notifyDataSetChanged()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.layoutBottomSheet.visibility = View.GONE
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

    private fun trackSelection(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(mapPlaylistToPlayer(track))
        )
    }

    private fun trackLongSelection(track: Track) {

    }

    private fun mapPlaylistToPlayer(track: Track): com.msaggik.playlistmaker.player.domain.models.Track {
        return with(track) {
            com.msaggik.playlistmaker.player.domain.models.Track(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                isFavorite = isFavorite,
                dateAddTrack = dateAddTrack
            )
        }
    }
}