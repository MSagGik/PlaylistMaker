package com.msaggik.player.domain.repository

import com.msaggik.player.domain.model.Track
import com.msaggik.player.domain.state.PlayerState
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

    suspend fun addFavoriteTrack(track: Track) : Long
    suspend fun deleteFavoriteTrack(track: Track) : Int
    fun getFavoriteTracksId() : Flow<List<Long>>
}