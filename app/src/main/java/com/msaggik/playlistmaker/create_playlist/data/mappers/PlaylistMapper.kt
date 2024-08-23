package com.msaggik.playlistmaker.create_playlist.data.mappers

import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.player.data.playlist_db.entity.many_to_many.PlaylistEntity

class PlaylistMapper {
    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                playlistUriAlbum = playlistUriAlbum
            )
        }
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return with(playlist) {
            Playlist(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                playlistUriAlbum = playlistUriAlbum
            )
        }
    }
}