package com.msaggik.search.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.common_util.Utils
import com.msaggik.common_ui.R
import com.msaggik.search.databinding.ItemTrackListBinding
import com.msaggik.search.domain.model.Track

class TrackListAdapter (
    trackListAdd: List<Track>,
    private val trackClickListener: TrackClickListener
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> () {

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