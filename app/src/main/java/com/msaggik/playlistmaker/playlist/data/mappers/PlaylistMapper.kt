package com.msaggik.playlistmaker.playlist.data.mappers

import com.msaggik.playlistmaker.playlist.domain.models.Playlist
import com.msaggik.playlistmaker.playlist.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.playlist.domain.models.Track

object PlaylistMapper {

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

    fun mapPlaylistToPlayer(track: Track): com.msaggik.playlistmaker.player.domain.models.Track {
        return with(track) {
            com.msaggik.playlistmaker.player.domain.models.Track(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                isFavorite = isFavorite,
                dateAddTrack = dateAddTrack
            )
        }
    }
    fun mapPlaylistToEditPlaylist(playlist: Playlist): com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist {
        return with(playlist) {
            com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist(
                playlistId = playlistId,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                playlistUriAlbum = playlistUriAlbum
            )
        }
    }
}