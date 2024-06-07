package com.msaggik.playlistmaker.player.data.impl

import android.content.Context
import android.media.MediaPlayer
import com.msaggik.playlistmaker.player.data.TrackPlayer
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.data.base.sp.impl.SearchHistorySpImpl
import com.msaggik.playlistmaker.search.domain.models.Track

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
            if (playerState == PlayerState.PLAYER_STATE_DEFAULT) {
                player.prepareAsync()
                player.setOnPreparedListener {
                    playerState = PlayerState.PLAYER_STATE_PREPARED
                }
            }
            setOnCompletionListener {
                playerState = PlayerState.PLAYER_STATE_PREPARED
            }
        }
    }

    override fun onPlay() {
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

    override fun loading(trackId: Int): Track {
        return Track(
            track!!.trackId, track!!.trackName, track!!.artistName,
            track!!.trackTimeMillis, track!!.artworkUrl100, track!!.collectionName,
            track!!.releaseDate, track!!.primaryGenreName, track!!.country, track!!.previewUrl
        )
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