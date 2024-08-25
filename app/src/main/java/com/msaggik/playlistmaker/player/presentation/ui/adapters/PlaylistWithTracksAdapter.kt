package com.msaggik.playlistmaker.player.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.ItemPlaylistSmallBinding
import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.util.Utils

class PlaylistWithTracksAdapter (
    private val trackId: Int,
    playlistsWithTracks: List<PlaylistWithTracks>,
    private val playlistClickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistWithTracksAdapter.PlaylistViewHolder> () {

    private var playlist = playlistsWithTracks

    fun setTrackList(playlistUpdate: List<PlaylistWithTracks>) {
        playlist = playlistUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistSmallBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(trackId, playlist[position])
        holder.itemView.setOnClickListener{
            playlistClickListener.onPlaylistClick(playlist.get(position))
        }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlistWithTracks: PlaylistWithTracks)
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistSmallBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(trackId: Int, model: PlaylistWithTracks) {
            Glide.with(itemView)
                .load(model.playlist.playlistUriAlbum)
                .placeholder(R.drawable.ic_placeholder)
                .transform(CenterCrop(), RoundedCorners(Utils.doToPx(3f, itemView.context.applicationContext)))
                .transform()
                .into(binding.imageAlbumPlaylist)
            binding.playlistName.text = model.playlist.playlistName
            binding.numberTracks.text = binding.root.context.getString(R.string.number_tracks, model.tracks.size)
            model.tracks.map { track ->
                if(track.trackId == trackId) {
                    binding.hasInPlaylist.setImageResource(R.drawable.ic_add_true)
                }
            }
        }
    }
}