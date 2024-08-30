package com.msaggik.playlistmaker.playlist.domain.use_case.impl

import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.repository.PlaylistRepository
import com.msaggik.playlistmaker.playlist.domain.use_case.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun playlistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> {
        return playlistRepository.playlistWithTracks(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(idPlaylist: Long, idTrack: Long): Int {
        return playlistRepository.removeTrackFromPlaylist(idPlaylist, idTrack)
    }

    override suspend fun sharePlaylist(infoPlaylist: String) {
        playlistRepository.sharePlaylist(infoPlaylist)
    }
}