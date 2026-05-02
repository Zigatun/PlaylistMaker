package ussr.playlistmaker.player.ui.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ussr.playlistmaker.media.domain.FavoritesInteractor
import ussr.playlistmaker.player.model.PlayerState
import ussr.playlistmaker.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivityViewModel(private val mediaPlayer: MediaPlayer, private val favoritesInteractor: FavoritesInteractor, val track: Track) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val observableTrackState: LiveData<PlayerState> = playerState

    private val isInFavorites = MutableLiveData<Boolean>(track.isFavorite)
    val observableIsInFavorites: LiveData<Boolean> = isInFavorites

    private var timerJob: Job? = null
    fun onPlayClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }
            else -> {}
        }
    }

    fun onFavoritesClicked(){
        viewModelScope.launch {
            if(isInFavorites.value == true){
                favoritesInteractor.removeTrack(track)
            } else {
                favoritesInteractor.addTrack(track)
            }
            isInFavorites.value?.let { isInFavorites.postValue(!it) }
        }
    }

    fun onPausePlayer(){
        pausePlayer()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            playerState.postValue(PlayerState.Prepared())
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(PLAYER_UPDATE_FREQ)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    init {
        preparePlayer(track.previewUrl)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.value = PlayerState.Default()
    }
    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }
    companion object {
        private const val PLAYER_UPDATE_FREQ = 300L
    }
}