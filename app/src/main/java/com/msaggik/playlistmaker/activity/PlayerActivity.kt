package com.msaggik.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView
    private lateinit var cover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackLength: TextView
    private lateinit var trackAlbumName: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView
    private lateinit var groupAlbumName: Group
    private lateinit var buttonPlayPause: ImageButton
    private lateinit var timeTrack: TextView

    private var player = MediaPlayer()
    private var playerState = PLAYER_STATE_CLEAR
    private var trackListReverse = true

    private val handlerTrackList = Handler(Looper.getMainLooper())

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        buttonBack = findViewById(R.id.button_back)
        cover = findViewById(R.id.cover)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        trackLength = findViewById(R.id.track_length)
        trackAlbumName = findViewById(R.id.track_album_name)
        trackYear = findViewById(R.id.track_year)
        trackGenre = findViewById(R.id.track_genre)
        trackCountry = findViewById(R.id.track_country)
        groupAlbumName = findViewById(R.id.group_album_name)
        buttonPlayPause = findViewById(R.id.button_play_pause)
        timeTrack = findViewById(R.id.time_track)

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
                .transform(RoundedCorners(doToPx(8f, this)))
                .into(cover)
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackLength.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(track.trackTimeMillis)
            if (track.collectionName.isNullOrEmpty()) {
                groupAlbumName.visibility = View.GONE
            } else {
                groupAlbumName.visibility = View.VISIBLE
                trackAlbumName.text = track.collectionName
            }
            trackYear.text =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(track.releaseDate)
                    ?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country

            preparePlayer(track.previewUrl)
        }

        buttonBack.setOnClickListener(listener)
        buttonPlayPause.setOnClickListener(listener)
        timeTrack.setOnClickListener(listener)
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
                            timeTrack.text = getString(R.string.time_track_reverse)
                        }
                    } else {
                        trackListReverse = true
                        if(playerState <= 1) {
                            timeTrack.text = getString(R.string.time_track)
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
                buttonPlayPause.isEnabled = true
                playerState = PLAYER_STATE_PREPARED
            }
            player.setOnCompletionListener {
                handlerTrackList.removeCallbacksAndMessages(null)
                buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_play))
                timeTrack.text = getString(R.string.time_track)
                playerState = PLAYER_STATE_PREPARED
            }
        }
    }

    private fun trackPlay() {
        player.start()
        buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_pause))
        playerState = PLAYER_STATE_PLAYING
        updateTrackList()
    }

    private fun trackPause() {
        handlerTrackList.removeCallbacksAndMessages(null)
        player.pause()
        buttonPlayPause.setImageDrawable(ContextCompat.getDrawable(this@PlayerActivity, R.drawable.ic_play))
        playerState = PLAYER_STATE_PAUSED
    }

    private fun updateTrackList() {
        handlerTrackList?.postDelayed(
            object : Runnable{
                @SuppressLint("SetTextI18n")
                override fun run() {
                    timeTrack.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(
                        if(trackListReverse) {
                            player.currentPosition
                        } else {
                            player.duration - player.currentPosition
                        })
                    handlerTrackList?.postDelayed(this, PLAYER_DELAY_UPDATE_TRACK_LIST)
                }
            }, PLAYER_DELAY_UPDATE_TRACK_LIST
        )
    }

    private fun doToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    companion object {
        private const val PLAYER_STATE_CLEAR = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
        private const val PLAYER_STATE_STOP = 4
        private const val PLAYER_DELAY_UPDATE_TRACK_LIST = 250L
    }
}