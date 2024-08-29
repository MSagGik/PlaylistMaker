package com.msaggik.playlistmaker.player.presentation.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.create_playlist.presentation.ui.CreatePlaylistFragment
import com.msaggik.playlistmaker.databinding.FragmentPlayerBinding
import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.presentation.ui.adapters.PlaylistWithTracksAdapter
import com.msaggik.playlistmaker.player.presentation.view_model.PlayerViewModel
import com.msaggik.playlistmaker.player.presentation.view_model.state.PlaylistWithTracksState
import com.msaggik.playlistmaker.util.Utils
import com.msaggik.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    companion object {
        private const val DELAY_CLICK_TRACK = 250L
        private const val TRACK_INSTANCE = "track_instance"
        fun createArgs(track: Track): Bundle {
            return bundleOf(
                TRACK_INSTANCE to track
            )
        }
    }

    // view-model
    private val playerViewModel: PlayerViewModel by viewModel()

    // view
    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!
    private var viewArray: Array<View>? = null

    // bottomSheetBehavior
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    // track
    private val track by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK_INSTANCE, Track::class.java)
        } else {
            requireArguments().getParcelable(TRACK_INSTANCE)
        }
    }

    // debounces
    private lateinit var setFavoriteClickDebounce: (Track) -> Unit
    private lateinit var createNewPlaylistClickDebounce: (Track) -> Unit

    // toast on/off
    private var hasToast = false

    // adapter playlists and listener select playlist to add track
    private var listPlaylistWithTracks: MutableList<PlaylistWithTracks> = mutableListOf()

    private val playlistWithTracksAdapter: PlaylistWithTracksAdapter by lazy {
        val trackId = track?.trackId ?: 0
        PlaylistWithTracksAdapter(trackId = trackId, listPlaylistWithTracks) {
            playlistWithTracksSelection(it)
        }
    }

    private fun playlistWithTracksSelection(playlist: PlaylistWithTracks) {
        hasToast = true
        track?.let {
            playerViewModel.addTrackInPlaylist(playlist.playlist, it)
        }
    }

    // fragment lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        viewArray = arrayOf(
            binding.placeholderEmptyPlaylists,
            binding.playlists
        )
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track?.let { playerViewModel.updateFavoriteStatusTrack(it) }

        initDebounces()

        // loading instance entity Track
        track?.let { playerViewModel.loadingTrack(it) }
        // creating an object of the BottomSheetBehavior class
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        // subscribe to display track metadata
        playerViewModel.getTrackLiveData().observe(viewLifecycleOwner) { track ->
            showTrackCover(track.artworkUrl100)
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackLength.text = Utils.dateFormatMillisToMinSecShort(track.trackTimeMillis)
            if (track.collectionName.isNullOrEmpty()) {
                binding.groupAlbumName.visibility = View.GONE
            } else {
                binding.groupAlbumName.visibility = View.VISIBLE
                binding.trackAlbumName.text = track.collectionName
            }
            binding.trackYear.text = Utils.dateFormatStandardToYear(track.releaseDate)
            binding.trackGenre.text = track.primaryGenreName
            binding.trackCountry.text = track.country
        }
        // subscribe to state button Play/Pause
        playerViewModel.getButtonStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.buttonPlayPause.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    state.stateViewButton
                )
            )
        }
        // subscribe to state track time progress
        playerViewModel.getCurrentTimePlayingLiveData().observe(viewLifecycleOwner) { currentTime ->
            binding.timeTrack.text = currentTime
        }
        // subscribe to status of track in favorites
        playerViewModel.getLikeStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.buttonLike.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    state.stateViewButton
                )
            )
        }
        // subscribe to state instance entity PlaylistWithTracksState
        playerViewModel.getPlaylistsWithTracksLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
        // subscribe to status of successful adding of a track to a playlist
        playerViewModel.getSuccessAddTrackInPlaylistLiveData()
            .observe(viewLifecycleOwner) { values ->
                val (idPlaylist, namePlaylist) = values
                val hasSuccessAddTrack = idPlaylist != -1L
                if (hasToast) {
                    val message = requireContext()
                        .getString(
                            if (hasSuccessAddTrack) R.string.add_track_in_playlist else R.string.no_add_track_in_playlist,
                            namePlaylist
                        )
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    hasToast = false
                }
                if (hasSuccessAddTrack) {
                    bottomSheetBehavior.apply {
                        state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }

        // connecting an adapter to display playlists
        binding.playlists.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.playlists.adapter = playlistWithTracksAdapter
        playlistWithTracksAdapter.notifyDataSetChanged()

        // listener installation
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        binding.buttonBack.setOnClickListener(listener)
        binding.buttonPlayPause.setOnClickListener(listener)
        binding.timeTrack.setOnClickListener(listener)
        binding.buttonLike.setOnClickListener(listener)
        binding.buttonAdd.setOnClickListener(listener)
        binding.buttonNewPlaylist.setOnClickListener(listener)
    }

    override fun onResume() {
        super.onResume()
        if (playerViewModel.buttonStatePrePlay) {
            playerViewModel.startPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hasToast = false
        _binding = null
        viewArray = emptyArray()
        viewArray = null
    }

    private fun render(state: PlaylistWithTracksState) {
        when (state) {
            is PlaylistWithTracksState.Content -> showPlaylistsWithTracks(state.playlists)
            is PlaylistWithTracksState.Empty -> Utils.visibilityView(
                viewArray,
                binding.placeholderEmptyPlaylists
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylistsWithTracks(playlists: List<PlaylistWithTracks>) {
        Utils.visibilityView(viewArray, binding.playlists)
        listPlaylistWithTracks.clear()
        listPlaylistWithTracks.addAll(playlists)
        playlistWithTracksAdapter.notifyDataSetChanged()
    }

    private fun showTrackCover(artworkUrl: String) {
        Glide.with(binding.cover)
            .load(artworkUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(Utils.doToPx(8f, requireContext())))
            .into(binding.cover)
    }

    private val bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        playerViewModel.pausePlayer()
                        playerViewModel.getPlaylistWithTracks()
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.main.alpha = 1 - slideOffset
            }
        }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    findNavController().popBackStack()
                }

                R.id.button_play_pause -> {
                    playerViewModel.checkPlayPause()
                }

                R.id.time_track -> {
                    playerViewModel.isReverse()
                }

                R.id.button_like -> {
                    track?.let { setFavoriteClickDebounce(it) }
                }

                R.id.button_add -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                R.id.button_new_playlist -> {
                    track?.let { createNewPlaylistClickDebounce(it) }
                }
            }
        }
    }

    private fun initDebounces() {
        setFavoriteClickDebounce = onTrackClickDebounceAll { playerViewModel.onFavoriteClicked(it) }
        createNewPlaylistClickDebounce = onTrackClickDebounceAll {
            findNavController().navigate(
                R.id.action_playerFragment_to_createPlaylistFragment,
                CreatePlaylistFragment.createArgs(true, mapPlayerToCreatePlaylist(track!!))
            )
        }
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

    private fun mapPlayerToCreatePlaylist(track: Track): com.msaggik.playlistmaker.create_playlist.domain.models.Track {
        return with(track) {
            com.msaggik.playlistmaker.create_playlist.domain.models.Track(
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