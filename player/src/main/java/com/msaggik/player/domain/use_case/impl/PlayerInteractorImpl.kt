package com.msaggik.player.domain.use_case.impl

import com.msaggik.player.domain.model.Track
import com.msaggik.player.domain.repository.TrackPlayer
import com.msaggik.player.domain.use_case.PlayerInteractor
import com.msaggik.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val trackPlayer: TrackPlayer
) : PlayerInteractor {

    // player
    override fun getPlayerState(): PlayerState {
        return trackPlayer.playerState
    }

    override fun play() {
        trackPlayer.onPlay()
    }

    override fun pause() {
        trackPlayer.onPause()
    }

    override fun stop() {
        trackPlayer.onStop()
    }

    override fun loading(trackId: Int) : Track {
        return trackPlayer.loading(trackId)
    }

    override fun getPlayerCurrentPosition(isReverse: Boolean): Long {
        return trackPlayer.getCurrentPosition(isReverse)
    }

    override fun reset() {
        trackPlayer.onReset()
    }

    override fun release() {
        trackPlayer.onRelease()
    }

    // favorite tracks
    override suspend fun addFavoriteTrack(track: Track): Long {
        return trackPlayer.addFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: Track): Int {
        return trackPlayer.deleteFavoriteTrack(track)
    }

    override fun getFavoriteTracksId(): Flow<List<Long>> {
        return trackPlayer.getFavoriteTracksId()
    }
}