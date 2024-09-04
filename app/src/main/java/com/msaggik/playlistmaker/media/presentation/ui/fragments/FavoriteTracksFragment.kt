package com.msaggik.playlistmaker.media.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.msaggik.playlistmaker.media.data.mappers.MediaMapper
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.presentation.ui.adapters.FavoriteTracksAdapter
import com.msaggik.playlistmaker.media.presentation.view_model.FavoriteTracksViewModel
import com.msaggik.playlistmaker.media.presentation.view_model.state.FavoriteTracksState
import com.msaggik.playlistmaker.player.presentation.ui.PlayerFragment
import com.msaggik.playlistmaker.util.Utils
import com.msaggik.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

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
            R.id.action_mediaFragment_to_playerFragment,
            PlayerFragment.createArgs(MediaMapper.mapMediaToPlayer(track))
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

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        const val DELAY_CLICK_TRACK = 1000L
    }
}