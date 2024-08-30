package com.msaggik.playlistmaker.playlist.data.mappers

import com.msaggik.playlistmaker.playlist.domain.models.Playlist
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.models.Track

class PlaylistMapper {

    fun mapPlaylistDbToPlaylist(playlist: com.msaggik.playlistmaker.player.data.playlist_db.entity.additional_entities.PlaylistWithTracks): PlaylistWithTracks {
        return with(playlist) {
            PlaylistWithTracks(
                playlist = with(playlistEntity) {
                    Playlist(
                        playlistId = playlistId,
                        playlistName = playlistName,
                        playlistDescription = playlistDescription,
                        playlistUriAlbum = playlistUriAlbum
                    )
                },
                tracks = tracks.map { trackEntity ->
                    with(trackEntity) {
                        Track(
                            trackId = trackId.toInt(),
                            trackName = trackName,
                            artistName = artistName,
                            trackTimeMillis = trackTimeMillis,
                            artworkUrl100 = artworkUrl100,
                            collectionName = collectionName,
                            releaseDate = releaseDate,
                            primaryGenreName = primaryGenreName,
                            country = country,
                            previewUrl = previewUrl,
                            isFavorite = favoriteTrack,
                            dateAddTrack = dateAddTrack
                        )
                    }
                }
            )
        }
    }
}