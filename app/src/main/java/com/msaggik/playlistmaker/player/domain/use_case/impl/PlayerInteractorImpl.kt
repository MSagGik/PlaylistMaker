package com.msaggik.playlistmaker.player.domain.use_case.impl

import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.domain.repository.PlayerRepository
import com.msaggik.playlistmaker.player.domain.use_case.PlayerInteractor
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
) : PlayerInteractor {
    override fun getPlayerState(): PlayerState {
        return playerRepository.playerState
    }

    override fun play() {
        playerRepository.onPlay()
    }

    override fun pause() {
        playerRepository.onPause()
    }

    override fun stop() {
        playerRepository.onStop()
    }

    override fun loading(previewUrl: String) {
        playerRepository.loading(previewUrl)
    }

    override fun getPlayerCurrentPosition(isReverse: Boolean): Long {
        return playerRepository.getCurrentPosition(isReverse)
    }

    override fun reset() {
        playerRepository.onReset()
    }

    override fun release() {
        playerRepository.onRelease()
    }

    // favorite tracks
    override suspend fun setFavoriteTrack(track: Track): Long {
        return playerRepository.setFavoriteTrack(track)
    }

    override fun getFavoriteTracksId(): Flow<List<Long>> {
        return playerRepository.getFavoriteTracksId()
    }

    override suspend fun isTrackInPlaylistAndTrack(idTrack: Long): Boolean {
        return playerRepository.isTrackInPlaylistAndTrack(idTrack)
    }

    override suspend fun playlistsWithTracks(): Flow<List<PlaylistWithTracks>> {
        return playerRepository.playlistsWithTracks()
    }

    override suspend fun addTrackInPlaylist(idPlaylist: Long, track: Track): Long {
        return playerRepository.addTrackInPlaylist(idPlaylist, track)
    }
}