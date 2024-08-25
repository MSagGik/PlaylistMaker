package com.msaggik.playlistmaker.media.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ItemPlaylistBinding
import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class PlaylistWithTracksAdapter (
    playlistsWithTracks: List<PlaylistWithTracks>,
    private val playlistClickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistWithTracksAdapter.PlaylistViewHolder> () {

    private var playlist = playlistsWithTracks

    fun setTrackList(playlistUpdate: List<PlaylistWithTracks>) {
        playlist = playlistUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener{
            playlistClickListener.onPlaylistClick(playlist.get(position))
        }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlistWithTracks: PlaylistWithTracks)
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PlaylistWithTracks) {
            Glide.with(itemView)
                .load(model.playlist.playlistUriAlbum)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(CenterCrop(), RoundedCorners(Utils.doToPx(8f, itemView.context.applicationContext)))
                .transform()
                .into(binding.coverPlaylist)
            binding.namePlaylist.text = model.playlist.playlistName
            val numberTracks = when(model.tracks.size) {
                1 -> binding.root.context.getString(R.string.number_tracks_a, model.tracks.size)
                2,3,4 -> binding.root.context.getString(R.string.number_tracks_b, model.tracks.size)
                else -> binding.root.context.getString(R.string.number_tracks_c, model.tracks.size)
            }
            binding.numberPlaylist.text = numberTracks
        }
    }
}