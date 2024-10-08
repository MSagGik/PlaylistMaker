package com.msaggik.playlistmaker.player.domain.use_case

import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun getPlayerState(): PlayerState
    fun play()
    fun pause()
    fun stop()
    fun loading(previewUrl: String)
    fun getPlayerCurrentPosition(isReverse: Boolean): Long
    fun reset()
    fun release()

    suspend fun setFavoriteTrack(track: Track) : Long
    fun getFavoriteTracksId() : Flow<List<Long>>

    suspend fun isTrackInPlaylistAndTrack(idTrack: Long) : Boolean
    suspend fun playlistsWithTracks(): Flow<List<PlaylistWithTracks>>

    suspend fun addTrackInPlaylist(idPlaylist: Long, track: Track): Long
}