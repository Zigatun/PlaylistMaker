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
import ussr.playlistmaker.media.ui.data.PlaylistsState
import ussr.playlistmaker.player.model.PlayerState
import ussr.playlistmaker.playlist.domain.PlaylistInteractor
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.models.TracksState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivityViewModel(
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    val track: Track
) : ViewModel() {

    private val playlistViewState = MutableLiveData<PlaylistsState>(PlaylistsState.Loading)
    fun observablePlaylistViewState(): LiveData<PlaylistsState> = playlistViewState

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default(track.isFavorite))
    val observableTrackState: LiveData<PlayerState> = playerState
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

    fun onFavoritesClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoritesInteractor.removeTrack(track)
                track.isFavorite = false
            } else {
                favoritesInteractor.addTrack(track)
                track.isFavorite = true
            }
            playerState.postValue(playerState.value.apply {
                this?.isInFavorites = track.isFavorite
            })
        }
    }


    fun onPausePlayer() {
        pausePlayer()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared(track.isFavorite))
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            playerState.postValue(PlayerState.Prepared(track.isFavorite))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition(), track.isFavorite))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition(), track.isFavorite))
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(PLAYER_UPDATE_FREQ)
                playerState.postValue(
                    PlayerState.Playing(
                        getCurrentPlayerPosition(),
                        track.isFavorite
                    )
                )
            }
        }
    }

    fun loadPlaylists(){
        viewModelScope.launch {
            playlistViewState.value = PlaylistsState.Loading
            playlistInteractor.GetPlaylists().collect { data ->
                if(data.isEmpty()){
                    playlistViewState.postValue(PlaylistsState.Empty("Вы не создали ни одного плейлиста"))
                }
                else{
                    playlistViewState.postValue(PlaylistsState.Content(data))
                }
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
        playerState.value = PlayerState.Default(track.isFavorite)
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    companion object {
        private const val PLAYER_UPDATE_FREQ = 300L
    }
}