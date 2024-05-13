package com.msaggik.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
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
            trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
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
        }

        buttonBack.setOnClickListener(listener)
    }

    private fun doToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when(p0?.id) {
                R.id.button_back -> {
                    finish()
                }
            }
        }
    }
}