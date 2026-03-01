package ussr.playlistmaker.player.ui.viewmodel

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Runnable
import ussr.playlistmaker.R
import ussr.playlistmaker.player.model.PlayerState
import ussr.playlistmaker.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivityViewModel(track: Track) : ViewModel() {

    private val trackState = MutableLiveData(PlayerState(track))
    val observableTrackState: LiveData<PlayerState> = trackState
    private var playerState = PLAYER_STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val playbackTimerRunnable = object : Runnable {
        override fun run() {
            if (playerState == PLAYER_STATE_PLAYING) {
                updateState { state ->
                    state.copy(
                        positionText = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                            mediaPlayer.currentPosition
                        )
                    )
                }
                handler.postDelayed(this, PLAYER_UPDATE_FREQ)
            }
        }
    }
    fun onPlayClicked() {
        when (playerState) {
            PLAYER_STATE_PLAYING -> pausePlayer()
            PLAYER_STATE_PREPARED,
            PLAYER_STATE_PAUSED -> startPlayer()
        }
    }

    fun onPausePlayer(){
        pausePlayer()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            playerState = PLAYER_STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PLAYER_STATE_PREPARED
            updateState { it.copy(isPlaying = false, positionText = "00:00") }
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PLAYER_STATE_PLAYING
        startTimer()

        updateState { it.copy(isPlaying = true) }
    }
    private fun startTimer() {
        handler.postDelayed(playbackTimerRunnable, PLAYER_UPDATE_FREQ)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PLAYER_STATE_PAUSED
        stopTimer()
        updateState { it.copy(isPlaying = false) }
    }

    private fun updateState(update: (PlayerState) -> PlayerState) {
        val current = trackState.value ?: return
        trackState.value = update(current)
    }

    private fun stopTimer() {
        handler.removeCallbacks(playbackTimerRunnable)
    }

    init {
        preparePlayer(track.previewUrl)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        stopTimer()
    }

    companion object {
        private const val PLAYER_STATE_DEFAULT = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
        private const val PLAYER_UPDATE_FREQ = 300L
        fun getFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerActivityViewModel(track)
            }
        }
    }
}