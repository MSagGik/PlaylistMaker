package com.msaggik.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.entity.Track

class TrackListAdapter (private val trackList: List<Track>) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track_list, parent, false)
        return TrackListAdapter.TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
    override fun getItemCount(): Int {
        return trackList.size
    }

    class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val imageAlbumTrack: ImageView = itemView.findViewById(R.id.image_album_track)
        private val trackName: TextView = itemView.findViewById(R.id.name_track)
        private val groupName: TextView = itemView.findViewById(R.id.group_name)
        private val trackLength: TextView = itemView.findViewById(R.id.length_track)
        private val buttonTrack: ImageView = itemView.findViewById(R.id.button_track)

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(doToPx(2f, itemView.context.applicationContext)))
                .into(imageAlbumTrack)
            trackName.text = model.trackName
            groupName.text = model.artistName
            trackLength.text = model.trackTime
            buttonTrack.setOnClickListener(View.OnClickListener {
                Toast.makeText(
                    itemView.context.applicationContext,
                    model.trackName,
                    Toast.LENGTH_SHORT
                ).show()
            })
        }

        private fun doToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }
    }
}