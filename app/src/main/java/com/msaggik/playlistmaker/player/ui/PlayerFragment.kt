package com.msaggik.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentPlayerBinding
import com.msaggik.playlistmaker.player.view_model.PlayerViewModel
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {

    companion object {

        private const val TRACK_INSTANCE = "track_instance"

        fun createArgs(track: Track): Bundle =
            bundleOf(
                TRACK_INSTANCE to track
            )
    }

    // view
    private lateinit var binding: FragmentPlayerBinding

    // track
    private val track by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(TRACK_INSTANCE, Track::class.java)
        } else {
            requireArguments().getSerializable(TRACK_INSTANCE)
        }
    }

    // view-model
    private val playerViewModel: PlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (track != null) {
            (track as Track).let { playerViewModel.loadingTrack(it.trackId) }
        }

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

        playerViewModel.getCurrentTimePlayingLiveData().observe(viewLifecycleOwner) { currentTime ->
            binding.timeTrack.text = currentTime
        }

        playerViewModel.getButtonStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.buttonPlayPause.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    state.stateViewButton
                )
            )
        }

        binding.buttonBack.setOnClickListener(listener)
        binding.buttonPlayPause.setOnClickListener(listener)
        binding.timeTrack.setOnClickListener(listener)
    }

    private fun showTrackCover(artworkUrl: String) {
        Glide.with(binding.cover)
            .load(artworkUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(Utils.doToPx(8f, requireContext())))
            .into(binding.cover)
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.releasePlayer()
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    findNavController().navigateUp()
                }

                R.id.button_play_pause -> {
                    playerViewModel.checkPlayPause()
                }

                R.id.time_track -> {
                    playerViewModel.isReverse()
                }
            }
        }
    }
}