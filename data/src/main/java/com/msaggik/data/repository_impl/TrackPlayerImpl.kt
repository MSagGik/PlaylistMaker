package com.msaggik.data.repository_impl

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.msaggik.data.api.db.favorite_tracks.TracksDatabase
import com.msaggik.data.api.network.dto.response.entities.TrackDto
import com.msaggik.data.api.network.mappers.PlayerMapper
import com.msaggik.data.local_util.Utils
import com.msaggik.player.domain.model.Track
import com.msaggik.player.domain.repository.TrackPlayer
import com.msaggik.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val DELTA_TIME_TRACK = 250L
private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"
class TrackPlayerImpl(
    private val mediaPlayer: MediaPlayer,
    private val spSearchHistory: SharedPreferences,
    private val converters: PlayerMapper,
    private val gson: Gson,
    private val tracksDataBase: TracksDatabase
) : TrackPlayer {

    // player

    override var playerState = PlayerState.PLAYER_STATE_DEFAULT

    private var trackListHistory: MutableList<TrackDto> = ArrayList()

    override fun onPlay() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYER_STATE_PLAYING
    }

    override fun onPause() {
        mediaPlayer.pause()
        playerState = PlayerState.PLAYER_STATE_PAUSED
    }

    override fun onStop() {
        mediaPlayer.stop()
        playerState = PlayerState.PLAYER_STATE_STOP
    }

    override fun loading(trackId: Int): Track {
        trackListHistory =
            Utils.readSharedPreferences(spSearchHistory, TRACK_LIST_HISTORY_KEY, gson)
        val track = converters.convertTrackDtoToTrack(
            track = Utils.searchTrackInList(
                trackId = trackId,
                list = trackListHistory
            )
        )
        playerInflate(track.previewUrl, mediaPlayer)
        return track
    }

    override fun getCurrentPosition(isReverse: Boolean): Long {
        return if(isReverse) {
            val reversTimeTrack = (mediaPlayer.duration - mediaPlayer.currentPosition).toLong()
            if(reversTimeTrack <= DELTA_TIME_TRACK) mediaPlayer.duration.toLong() else reversTimeTrack
        } else {
            val timeTrack = mediaPlayer.currentPosition.toLong()
            if(timeTrack >= mediaPlayer.duration.toLong() - DELTA_TIME_TRACK) 0L else timeTrack
        }
    }

    override fun onReset() {
        mediaPlayer.reset()
        playerState = PlayerState.PLAYER_STATE_DEFAULT
    }

    override fun onRelease() {
        mediaPlayer.release()
        playerState = PlayerState.PLAYER_STATE_DEFAULT
    }

    private fun playerInflate(previewUrl: String, player: MediaPlayer) {
        player.apply {
            setDataSource(previewUrl)
            if (playerState == PlayerState.PLAYER_STATE_DEFAULT) {
                prepareAsync()
                setOnPreparedListener {
                    playerState = PlayerState.PLAYER_STATE_PREPARED
                }
            }
            setOnCompletionListener {
                playerState = PlayerState.PLAYER_STATE_PREPARED
            }
        }
    }

    // favorite tracks

    override suspend fun addFavoriteTrack(track: Track): Long {
        return tracksDataBase.favoriteTracksDao().insertTrack(converters.map(track))
    }

    override suspend fun deleteFavoriteTrack(track: Track): Int {
        return tracksDataBase.favoriteTracksDao().deleteTrack(converters.map(track))
    }

    override fun getFavoriteTracksId(): Flow<List<Long>> = flow {
        emit(
            tracksDataBase
                .favoriteTracksDao()
                .getFavoriteTracksIds()
        )
    }
}