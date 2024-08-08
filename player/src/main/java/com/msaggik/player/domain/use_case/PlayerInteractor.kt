package com.msaggik.player.domain.use_case

import com.msaggik.player.domain.model.Track
import com.msaggik.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun getPlayerState(): PlayerState
    fun play()
    fun pause()
    fun stop()
    fun loading(trackId: Int) : Track
    fun getPlayerCurrentPosition(isReverse: Boolean): Long
    fun reset()
    fun release()

    suspend fun addFavoriteTrack(track: Track) : Long

    suspend fun deleteFavoriteTrack(track: Track) : Int

    fun getFavoriteTracksId() : Flow<List<Long>>
}