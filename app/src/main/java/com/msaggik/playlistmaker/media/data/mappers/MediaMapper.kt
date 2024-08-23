package com.msaggik.playlistmaker.media.data.mappers

import com.msaggik.playlistmaker.media.domain.models.Playlist
import com.msaggik.playlistmaker.media.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.media.domain.models.Track
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity

class MediaMapper {

    fun mapTrackEntityToTrack(trackEntity: TrackEntity): Track {
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
                dateAddTrack = dateAddTrack
            )
        }
    }

    fun mapTrackToTrackEntity(track: Track): TrackEntity {
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
                dateAddTrack = dateAddTrack
            )
        }
    }

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

    fun mapPlaylistToPlaylistDb(playlists: PlaylistWithTracks): com.msaggik.playlistmaker.player.data.playlist_db.entity.additional_entities.PlaylistWithTracks{
        return with(playlists) {
            com.msaggik.playlistmaker.player.data.playlist_db.entity.additional_entities.PlaylistWithTracks(
                playlistEntity = with(playlist) {
                    PlaylistEntity(
                        playlistId = playlistId,
                        playlistName = playlistName,
                        playlistDescription = playlistDescription,
                        playlistUriAlbum = playlistUriAlbum
                    )
                },
                tracks = tracks.map { track ->
                    with(track) {
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
            )
        }
    }
}