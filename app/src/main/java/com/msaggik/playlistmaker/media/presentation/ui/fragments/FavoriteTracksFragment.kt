package com.msaggik.playlistmaker.media.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaggik.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.media.presentation.ui.adapters.FavoriteTracksAdapter
import com.msaggik.playlistmaker.media.presentation.view_model.FavoriteTracksViewModel
import com.msaggik.playlistmaker.media.presentation.view_model.state.FavoriteTracksState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private lateinit var binding: FragmentFavoriteTracksBinding

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    private var listMediaTracks: MutableList<Track> = ArrayList()

    private val trackListAdapter: FavoriteTracksAdapter by lazy {
        FavoriteTracksAdapter(listMediaTracks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = trackListAdapter

        favoriteTracksViewModel.getTrackListMediaLiveData().observe(viewLifecycleOwner) {
            listMediaTracks.clear()
            render(it)
        }
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showFavoriteTracks(state.trackList)
            is FavoriteTracksState.Empty -> showEmptyMessage()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavoriteTracks(favoriteTracks: List<Track>) {
        binding.apply {
            placeholderEmptyMediaLibrary.visibility = View.GONE
            content.visibility = View.VISIBLE
            listMediaTracks.addAll(favoriteTracks)
            trackListAdapter.notifyDataSetChanged()
        }
    }

    private fun showEmptyMessage() {
        binding.apply {
            placeholderEmptyMediaLibrary.visibility = View.VISIBLE
            content.visibility = View.GONE
        }
    }
}