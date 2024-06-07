package com.msaggik.playlistmaker.player.data.impl

import android.content.Context
import android.media.MediaPlayer
import com.msaggik.playlistmaker.player.data.TrackPlayer
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl

class TrackPlayerImpl(private val trackId: Int, private val context: Context) : TrackPlayer {

    private val player = MediaPlayer()

    override var playerState = PlayerState.PLAYER_STATE_DEFAULT

    private val tracksHistory by lazy {
        SearchHistorySpImpl(context).readTrackListHistorySharedPreferences()
    }

    private val track by lazy {
        tracksHistory.firstOrNull { track ->
            track.trackId == trackId
        }
    }

    init {
        player.apply {
            setDataSource(track?.previewUrl)
            setOnCompletionListener {
                playerState = PlayerState.PLAYER_STATE_PREPARED
            }
        }
    }

    override fun onPlay() {
        if (playerState == PlayerState.PLAYER_STATE_DEFAULT) {
            player.prepareAsync()
            playerState = PlayerState.PLAYER_STATE_PREPARED
        }
        player.start()
        playerState = PlayerState.PLAYER_STATE_PLAYING
    }

    override fun onPause() {
        player.pause()
        playerState = PlayerState.PLAYER_STATE_PAUSED
    }

    override fun onStop() {
        player.stop()
        playerState = PlayerState.PLAYER_STATE_STOP
    }

    override fun getCurrentPosition(isReverse: Boolean): Int {
        return if(isReverse) {
            player.duration - player.currentPosition
        } else {
            player.currentPosition
        }
    }

    override fun onRelease() {
        player.release()
        playerState = PlayerState.PLAYER_STATE_DEFAULT
    }
}