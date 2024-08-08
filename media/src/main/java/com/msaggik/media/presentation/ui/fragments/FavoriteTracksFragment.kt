package com.msaggik.media.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaggik.common_util.Utils
import com.msaggik.common_util.debounce
import com.msaggik.media.databinding.FragmentFavoriteTracksBinding
import com.msaggik.media.domain.models.Track
import com.msaggik.media.presentation.ui.adapters.FavoriteTracksAdapter
import com.msaggik.player.presentation.ui.PlayerFragment
import com.msaggik.media.presentation.view_model.FavoriteTracksViewModel
import com.msaggik.playlistmaker.media.presentation.view_model.state.FavoriteTracksState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteTracksFragment()
        const val DELAY_CLICK_TRACK = 1000L
    }

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    private var viewArray: Array<View>? = null
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding: FragmentFavoriteTracksBinding get() = _binding!!

    private var listMediaTracks: MutableList<Track> = ArrayList()

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val trackListAdapter: FavoriteTracksAdapter by lazy {
        FavoriteTracksAdapter(listMediaTracks) {
            onTrackClickDebounce(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        viewArray = arrayOf(
            binding.placeholderEmptyMediaLibrary,
            binding.content
        )
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            DELAY_CLICK_TRACK,
            viewLifecycleOwner.lifecycleScope,
            false,
            true
        ) { track -> trackSelection(track) }

        binding.trackList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = trackListAdapter
        trackListAdapter.notifyDataSetChanged()

        favoriteTracksViewModel.getFavoriteTrackListLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackSelection(track: Track) {
        track.apply {
            isFavorite = true
            dateAddTrack = System.currentTimeMillis()
        }
        favoriteTracksViewModel.addTrackListHistory(track)
        findNavController().navigate(
            com.msaggik.common_ui.R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(mapMediaToPlayer(track))
        )
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.getFavoriteTrackList()
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showFavoriteTracks(state.trackList)
            is FavoriteTracksState.Empty -> Utils.visibilityView(viewArray, binding.placeholderEmptyMediaLibrary)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavoriteTracks(favoriteTracks: List<Track>) {
        Utils.visibilityView(viewArray, binding.content)
        listMediaTracks.clear()
        listMediaTracks.addAll(favoriteTracks)
        trackListAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewArray = emptyArray()
        viewArray = null
    }

    private fun mapMediaToPlayer(track: Track): com.msaggik.player.domain.model.Track {
        return with(track) {
            com.msaggik.player.domain.model.Track(
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