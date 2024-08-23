package com.msaggik.playlistmaker.media.domain.use_case

import com.msaggik.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaInteractor {
    suspend fun addFavoriteTrack(track: Track) : Long
    fun getFavoriteTracks() : Flow<List<Track>>
}