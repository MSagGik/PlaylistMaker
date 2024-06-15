package com.msaggik.playlistmaker.player.data

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.msaggik.playlistmaker.player.domain.repository.TrackPlayer
import com.msaggik.playlistmaker.player.domain.state.PlayerState
import com.msaggik.playlistmaker.search.data.dto.response.TrackDto
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.util.Utils
import org.koin.core.component.KoinComponent

private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"
class TrackPlayerImpl(
    private val mediaPlayer: MediaPlayer,
    private val spSearchHistory: SharedPreferences,
    private val gson: Gson
) : TrackPlayer, KoinComponent {

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
        trackListHistory = Utils.readSharedPreferences(spSearchHistory, TRACK_LIST_HISTORY_KEY, gson)
        val track = Utils.convertTrackDtoToTrack(
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