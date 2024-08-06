package com.msaggik.playlistmaker.media.domain.repository

import com.msaggik.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addFavoriteTrack(track: Track) : Long

    suspend fun deleteFavoriteTrack(track: Track) : Int

    fun getFavoriteTracks() : Flow<List<Track>>

}