package com.msaggik.media.domain.repository

import com.msaggik.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addFavoriteTrack(track: Track) : Long

    fun getFavoriteTracks() : Flow<List<Track>>

}