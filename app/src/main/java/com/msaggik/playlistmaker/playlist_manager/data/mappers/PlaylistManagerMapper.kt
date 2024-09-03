package com.msaggik.playlistmaker.playlist_manager.data.mappers

import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

object PlaylistManagerMapper {
    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                playlistId = playlistId,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                playlistUriAlbum = playlistUriAlbum
            )
        }
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return with(playlist) {
            Playlist(
                playlistId = playlistId,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                playlistUriAlbum = playlistUriAlbum
            )
        }
    }

    fun map(trackEntity: TrackEntity): Track {
        return with(trackEntity) {
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
                dateAddTrack = dateAddTrack,
                isFavorite = favoriteTrack
            )
        }
    }

    fun map(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                trackId = trackId.toLong(),
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                favoriteTrack = isFavorite,
                dateAddTrack = dateAddTrack
            )
        }
    }
}