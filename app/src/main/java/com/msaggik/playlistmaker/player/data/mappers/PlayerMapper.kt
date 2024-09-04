package com.msaggik.playlistmaker.player.data.mappers

import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.TrackEntity
import com.msaggik.playlistmaker.player.domain.models.Playlist
import com.msaggik.playlistmaker.player.domain.models.PlaylistWithTracks
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.search.data.dto.response.TrackDto

object PlayerMapper {
    fun convertTrackDtoToTrack(track: TrackDto): Track {
        return Track(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
    }
    fun convertTrackToTrackDto(track: Track): TrackDto {
        return TrackDto(
            track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
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

    fun mapPlayerToCreatePlaylist(track: Track): com.msaggik.playlistmaker.playlist_manager.domain.models.Track {
        return with(track) {
            com.msaggik.playlistmaker.playlist_manager.domain.models.Track(
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
}