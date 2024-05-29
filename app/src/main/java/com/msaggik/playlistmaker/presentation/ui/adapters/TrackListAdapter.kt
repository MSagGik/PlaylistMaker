package com.msaggik.playlistmaker.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.domain.models.Track
import com.msaggik.playlistmaker.util.Utils
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListAdapter (private val trackListAdd: List<Track>, private val trackClickListener: TrackClickListener) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> () {

    private var trackList = trackListAdd

    fun setTrackList(trackListUpdate: List<Track>) {
        trackList = trackListUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track_list, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            trackClickListener.onTrackClick(trackList.get(position))
        }
    }
    override fun getItemCount(): Int {
        return trackList.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val imageAlbumTrack: ImageView = itemView.findViewById(R.id.image_album_track)
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val groupName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackLength: TextView = itemView.findViewById(R.id.length_track)

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(Utils.doToPx(2f, itemView.context.applicationContext)))
                .into(imageAlbumTrack)
            trackName.text = model.trackName
            groupName.text = model.artistName
            trackLength.text = Utils.dateFormatMillisToMinSecFull(model.trackTimeMillis)
        }
    }
}