package com.msaggik.playlistmaker.media.domain.repository

import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun addFavoriteTrack(track: Track) : Long

    fun getFavoriteTracks() : Flow<List<Track>>

    fun playlistsWithTracks(): Flow<List<PlaylistWithTracks>>

}