package com.msaggik.playlistmaker.player.data

import android.content.Context
import android.media.MediaPlayer
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils

class TrackPlayerImpl(
    private val context: Context,
    private val mediaPlayer: MediaPlayer
) : TrackPlayer {

    override var playerState = PlayerState.PLAYER_STATE_DEFAULT

    private val tracksHistory by lazy {
        SearchHistorySpImpl(context).readTrackListHistorySharedPreferences()
    }

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
        val track = Utils.convertTrackDtoToTrack(Utils.searchTrackInList(trackId, tracksHistory))
        playerInflate(track.previewUrl, mediaPlayer)
        return track
    }

    override fun getCurrentPosition(isReverse: Boolean): Int {
        return if(isReverse) {
            mediaPlayer.duration - mediaPlayer.currentPosition
        } else {
            mediaPlayer.currentPosition
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
}