package com.msaggik.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ActivityPlayerBinding
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class PlayerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private var player = MediaPlayer()
    private var playerState = PLAYER_STATE_CLEAR
    private var trackListReverse = true

    private val handlerTrackList = Handler(Looper.getMainLooper())

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val track = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Track::class.java.simpleName, Track::class.java)
        } else {
            intent.getSerializableExtra(Track::class.java.simpleName) as Track
        }

        if (track != null) {
            Glide.with(this)
                .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(Utils.doToPx(8f, this)))
                .into(binding.cover)
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

            preparePlayer(track.previewUrl)
        }

        binding.buttonBack.setOnClickListener(listener)
        binding.buttonPlayPause.setOnClickListener(listener)
        binding.timeTrack.setOnClickListener(listener)
    }

    override fun onPause() {
        super.onPause()
        trackPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when(p0?.id) {
                R.id.button_back -> {
                    finish()
                }
                R.id.button_play_pause -> {
                    when(playerState) {
                        PLAYER_STATE_PREPARED, PLAYER_STATE_PAUSED, PLAYER_STATE_STOP -> {
                            trackPlay()
                        }
                        PLAYER_STATE_PLAYING -> {
                            trackPause()
                        }
                    }
                }
                R.id.time_track -> {
                    if (trackListReverse) {
                        trackListReverse = false
                        if(playerState <= 1) {
                            binding.timeTrack.text = getString(R.string.time_track_reverse)
                        }
                    } else {
                        trackListReverse = true
                        if(playerState <= 1) {
                            binding.timeTrack.text = getString(R.string.time_track)
                        }
                    }
                }
            }
        }
    }

    private fun preparePlayer(urlTrack: String?) {
        if(!urlTrack.isNullOrEmpty()) {
            player.setDataSource(urlTrack)
            player.prepareAsync()
            player.setOnPreparedListener {
                binding.buttonPlayPause.isEnabled = true
                playerState = PLAYER_STATE_PREPARED
            }
            player.setOnCompletionListener {
                handlerTrackList.removeCallbacksAndMessages(null)
                binding.buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_play))
                binding.timeTrack.text = getString(R.string.time_track)
                playerState = PLAYER_STATE_PREPARED
            }
        }
    }

    private fun trackPlay() {
        player.start()
        binding.buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_pause))
        playerState = PLAYER_STATE_PLAYING
        updateTrackList()
    }

    private fun trackPause() {
        handlerTrackList.removeCallbacksAndMessages(null)
        player.pause()
        binding.buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_play))
        playerState = PLAYER_STATE_PAUSED
    }

    private fun updateTrackList() {
        handlerTrackList.postDelayed(
            object : Runnable{
                @SuppressLint("SetTextI18n")
                override fun run() {
                    binding.timeTrack.text = Utils.dateFormatMillisToMinSecShort(
                        if(trackListReverse) {
                            player.currentPosition.toLong()
                        } else {
                            (player.duration - player.currentPosition).toLong()
                        })
                    handlerTrackList.postDelayed(this, PLAYER_DELAY_UPDATE_TRACK_LIST)
                }
            }, PLAYER_DELAY_UPDATE_TRACK_LIST
        )
    }

    private companion object {
        private const val PLAYER_STATE_CLEAR = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
        private const val PLAYER_STATE_STOP = 4
        private const val PLAYER_DELAY_UPDATE_TRACK_LIST = 250L
    }
}