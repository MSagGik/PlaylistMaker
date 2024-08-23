package com.msaggik.playlistmaker.player.domain.repository

import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow

interface TrackPlayer {
    var playerState: PlayerState
    fun onPlay()
    fun onPause()
    fun onStop()
    fun loading(previewUrl: String)
    fun getCurrentPosition(isReverse: Boolean): Long
    fun onReset()
    fun onRelease()

    suspend fun setFavoriteTrack(track: Track) : Long

    fun getFavoriteTracksId() : Flow<List<Long>>
}