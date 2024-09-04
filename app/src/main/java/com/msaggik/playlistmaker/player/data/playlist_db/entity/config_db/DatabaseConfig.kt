package com.msaggik.playlistmaker.player.data.playlist_db.entity.config_db

object DatabaseConfig {

    const val DATABASE_NAME: String = "playlist_maker.db"

    // PLAYLIST
    const val PLAYLIST_TABLE: String = "playlist_table"

    const val PLAYLIST_ID: String = "playlist_id"
    const val PLAYLIST_NAME: String = "playlist_name"
    const val PLAYLIST_DESCRIPTION: String = "playlist_description"
    const val PLAYLIST_URI_ALBUM: String = "playlist_uri_album"

    // TRACK
    const val TRACK_TABLE: String = "track_table"

    const val TRACK_ID: String = "track_id"
    const val TRACK_NAME: String = "track_name"
    const val ARTIST_NAME: String = "artist_name"
    const val TRACK_TIME_MILLIS: String = "track_time_millis"
    const val ARTWORK_URL: String = "artwork_url"
    const val COLLECTION_NAME: String = "collection_name"
    const val RELEASE_DATE: String = "release_date"
    const val PRIMARY_GENRE_NAME: String = "primary_genre_name"
    const val PREVIEW_URL: String = "preview_url"
    const val DATE_ADD_TRACK: String = "date_add_track"
    const val FAVORITE_TRACK: String = "favorite_track"

    // TRACK
    const val PLAYLIST_AND_TRACK_TABLE: String = "playlist_and_track_table"

    const val ID_TRACK: String = "id_track"
    const val ID_PLAYLIST: String = "id_playlist"
    const val TIME_ADD: String = "time_add"
}