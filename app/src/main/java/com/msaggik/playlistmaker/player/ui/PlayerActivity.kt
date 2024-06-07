package com.msaggik.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ActivityPlayerBinding
import com.msaggik.playlistmaker.player.view_model.PlayerViewModel
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class PlayerActivity : AppCompatActivity() {

    // view
    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    // track
    private val track by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Track::class.java.simpleName, Track::class.java)
        } else {
            intent.getSerializableExtra(Track::class.java.simpleName) as Track
        }
    }

    // view-model
    private val playerViewModel by lazy {
        ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track?.trackId ?: -1))[PlayerViewModel::class.java]
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        track?.let { playerViewModel.loadingTrack(it.trackId) }

        playerViewModel.getTrackLiveData().observe(this) { track ->
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

        playerViewModel.getCurrentTimePlayingLiveData().observe(this) { currentTime ->
            binding.timeTrack.text = currentTime
        }

        playerViewModel.getButtonStateLiveData().observe(this) { state ->
            binding.buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, state.stateViewButton))
        }

        binding.buttonBack.setOnClickListener(listener)
        binding.buttonPlayPause.setOnClickListener(listener)
        binding.timeTrack.setOnClickListener(listener)
    }

    private fun showTrackCover(artworkUrl: String) {
        Glide.with(binding.cover)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(Utils.doToPx(8f, this)))
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
                    finish()
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