package ussr.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ussr.playlistmaker.media.ui.data.PlaylistsState
import ussr.playlistmaker.playlist.domain.PlaylistInteractor

class UserPlaylistsFragmentViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val playlistViewState = MutableLiveData<PlaylistsState>(PlaylistsState.Loading)
    fun observablePlaylistViewState(): LiveData<PlaylistsState> = playlistViewState

    private fun loadFavorites(){
        viewModelScope.launch {
            playlistViewState.value = PlaylistsState.Loading
            playlistInteractor.getPlaylists().collect { data ->
                if(data.isEmpty()){
                    playlistViewState.postValue(PlaylistsState.Empty("Вы не создали ни одного плейлиста"))
                }
                else{
                    playlistViewState.postValue(PlaylistsState.Content(data))
                }
            }
        }
    }

    fun onViewPrepared(){
        loadFavorites()
    }
}