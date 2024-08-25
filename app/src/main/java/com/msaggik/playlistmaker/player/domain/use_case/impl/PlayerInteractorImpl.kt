package com.msaggik.playlistmaker.player.domain.use_case.impl

import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val trackPlayer: TrackPlayer
) : PlayerInteractor {
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

    override fun loading(previewUrl: String) {
        trackPlayer.loading(previewUrl)
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
    override suspend fun setFavoriteTrack(track: Track): Long {
        return trackPlayer.setFavoriteTrack(track)
    }

    override fun getFavoriteTracksId(): Flow<List<Long>> {
        return trackPlayer.getFavoriteTracksId()
    }

    override suspend fun isTrackInPlaylistAndTrack(idTrack: Long): Boolean {
        return trackPlayer.isTrackInPlaylistAndTrack(idTrack)
    }

    override suspend fun playlistsWithTracks(): Flow<List<PlaylistWithTracks>> {
        return trackPlayer.playlistsWithTracks()
    }

    override suspend fun addTrackInPlaylist(idPlaylist: Long, track: Track): Long {
        return trackPlayer.addTrackInPlaylist(idPlaylist, track)
    }
}