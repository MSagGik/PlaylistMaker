package com.msaggik.playlistmaker.util.adapters

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.activity.PlayerActivity
import com.msaggik.playlistmaker.entity.Track
import com.msaggik.playlistmaker.util.additionally.SearchHistory
import java.text.SimpleDateFormat
import java.util.Locale

private const val TRACK_LIST_PREFERENCES = "track_list_preferences"

class TrackListAdapter (private val trackListAdd: List<Track>) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> () {

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
    }
    override fun getItemCount(): Int {
        return trackList.size
    }

    class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val imageAlbumTrack: ImageView = itemView.findViewById(R.id.image_album_track)
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val groupName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackLength: TextView = itemView.findViewById(R.id.length_track)
        private val buttonTrack: ImageView = itemView.findViewById(R.id.button_track)
        private val layoutTrack: FrameLayout = itemView.findViewById(R.id.layout_track)

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(doToPx(2f, itemView.context.applicationContext)))
                .into(imageAlbumTrack)
            trackName.text = model.trackName
            groupName.text = model.artistName
            trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
            layoutTrack.setOnClickListener(View.OnClickListener {
                // получение объекта настроек и их обновление новым треком
                val sharedPreferences = itemView.context.applicationContext.getSharedPreferences(TRACK_LIST_PREFERENCES, Context.MODE_PRIVATE)
                val searchHistory = SearchHistory()
                searchHistory.addTrackListHistorySharedPreferences(sharedPreferences, model)
                // переход в активность аудиоплеера
                val context = itemView.context
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra(Track::class.java.simpleName, model)
                context.startActivity(intent)
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