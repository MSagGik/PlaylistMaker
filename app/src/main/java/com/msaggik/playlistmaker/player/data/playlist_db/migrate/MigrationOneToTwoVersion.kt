package com.msaggik.playlistmaker.player.data.playlist_db.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationOneToTwoVersion: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // new table
        db.execSQL("""
            CREATE TABLE playlist_and_track_table_new (
                id_playlist INTEGER NOT NULL,
                id_track INTEGER NOT NULL,
                time_add INTEGER NOT NULL DEFAULT (strftime('%s', 'now') * 1000),
                PRIMARY KEY(id_playlist, id_track)
            )
        """.trimIndent())

        // copy old table
        db.execSQL("""
            INSERT INTO playlist_and_track_table_new (id_playlist, id_track, time_add)
            SELECT id_playlist, id_track, strftime('%s', 'now') * 1000 FROM playlist_and_track_table
        """.trimIndent())

        // delete old table
        db.execSQL("DROP TABLE playlist_and_track_table")

        // refactor name new table
        db.execSQL("ALTER TABLE playlist_and_track_table_new RENAME TO playlist_and_track_table")
    }
}