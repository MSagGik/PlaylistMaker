package com.msaggik.playlistmaker.media.domain.repository

import com.msaggik.playlistmaker.media.domain.models.Track
interface MediaRepository {
    fun readTrackListHistory() : List<Track>
}