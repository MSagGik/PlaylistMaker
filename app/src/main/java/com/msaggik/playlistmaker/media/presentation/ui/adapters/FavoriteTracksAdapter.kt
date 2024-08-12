package com.msaggik.playlistmaker.media.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ItemTrackListBinding
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class FavoriteTracksAdapter (
    trackListAdd: List<Track>,
    private val trackClickListener: TrackClickListener
) : RecyclerView.Adapter<FavoriteTracksAdapter.TrackViewHolder> () {

    private var trackList = trackListAdd

    fun setTrackList(trackListUpdate: List<Track>) {
        trackList = trackListUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(ItemTrackListBinding.inflate(layoutInspector, parent, false))
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

    class TrackViewHolder(private val binding: ItemTrackListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(Utils.doToPx(2f, itemView.context.applicationContext)))
                .into(binding.imageAlbumTrack)
            binding.trackName.text = model.trackName
            binding.artistName.text = model.artistName
            binding.lengthTrack.text = Utils.dateFormatMillisToMinSecFull(model.trackTimeMillis)
        }
    }
}