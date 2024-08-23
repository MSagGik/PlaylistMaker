package com.msaggik.playlistmaker.player.data.repository_impl

import android.media.MediaPlayer
import com.msaggik.playlistmaker.player.data.mappers.PlayerMapper
import com.msaggik.playlistmaker.player.data.playlist_db.PlaylistTracksDatabase
import com.msaggik.playlistmaker.player.domain.models.Track
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val DELTA_TIME_TRACK = 250L
class TrackPlayerImpl(
    private val mediaPlayer: MediaPlayer,
    private val converters: PlayerMapper,
    private val dataBase: PlaylistTracksDatabase
) : TrackPlayer {

    override var playerState = PlayerState.PLAYER_STATE_DEFAULT

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

    override fun loading(previewUrl: String) {
        playerInflate(previewUrl, mediaPlayer)
    }

    override fun getCurrentPosition(isReverse: Boolean): Long {
        return if (isReverse) {
            val reversTimeTrack = (mediaPlayer.duration - mediaPlayer.currentPosition).toLong()
            if (reversTimeTrack <= DELTA_TIME_TRACK) mediaPlayer.duration.toLong() else reversTimeTrack
        } else {
            val timeTrack = mediaPlayer.currentPosition.toLong()
            if (timeTrack >= mediaPlayer.duration.toLong() - DELTA_TIME_TRACK) 0L else timeTrack
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

    override suspend fun setFavoriteTrack(track: Track): Long {
        return dataBase.playlistTracksDao().setFavoriteTrack(converters.map(track))
    }

//    override suspend fun addFavoriteTrack(track: Track): Long {
//        return dataBase.playlistTracksDao().setFavoriteTrack(converters.map(track))
//    }
//
//    override suspend fun deleteFavoriteTrack(track: Track): Int {
//        return dataBase.playlistTracksDao().setFavoriteTrack(converters.map(track)).toInt()
//    }

    override fun getFavoriteTracksId(): Flow<List<Long>> = flow {
        emit(
            dataBase
                .playlistTracksDao()
                .getFavoriteTracksIds()
        )
    }
}