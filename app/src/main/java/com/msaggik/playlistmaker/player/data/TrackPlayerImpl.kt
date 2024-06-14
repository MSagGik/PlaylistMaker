package com.msaggik.playlistmaker.player.data

import android.media.MediaPlayer
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.data.base.sp.SearchHistorySp
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TrackPlayerImpl(
    private val mediaPlayer: MediaPlayer
) : TrackPlayer, KoinComponent {

    override var playerState = PlayerState.PLAYER_STATE_DEFAULT

    private val searchHistorySp: SearchHistorySp by inject()
    private val tracksHistory by lazy {
        searchHistorySp.readTrackListHistorySharedPreferences()
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

    override fun getCurrentPosition(isReverse: Boolean): Long {
        return if(isReverse) {
            (mediaPlayer.duration - mediaPlayer.currentPosition).toLong()
        } else {
            mediaPlayer.currentPosition.toLong()
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