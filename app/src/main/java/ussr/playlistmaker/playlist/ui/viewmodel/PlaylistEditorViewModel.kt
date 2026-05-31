package ussr.playlistmaker.playlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.domain.PlaylistContentRepository
import ussr.playlistmaker.playlist.domain.PlaylistInteractor
import ussr.playlistmaker.playlist.ui.data.SharePlaylistEvent
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.ui.Event

class PlaylistEditorViewModel(private val playlistInteractor: PlaylistInteractor,
                              private val playlistContentRepository: PlaylistContentRepository,
                              private val playlist: PlaylistModel) : ViewModel() {
    private val _playlistContentState = MutableLiveData<List<Track>>()
    fun PlaylistContentState(): LiveData<List<Track>> = _playlistContentState

    private val _trackNavigationEvent = MutableLiveData<Event<Track>>()
    fun TrackNavigationEvent(): LiveData<Event<Track>> = _trackNavigationEvent

    private val _playlistInfoNavigationEvent = MutableLiveData<Event<PlaylistModel>>()
    fun PlaylistInfoNavigationEvent(): LiveData<Event<PlaylistModel>> = _playlistInfoNavigationEvent

    private val _trackRemovingEvent = MutableLiveData<Event<Track>>()
    fun TrackRemovingEvent(): LiveData<Event<Track>> = _trackRemovingEvent

    private val _playlistRemovingEvent = MutableLiveData<Event<PlaylistModel>>()
    fun PlaylistRemovingEvent(): LiveData<Event<PlaylistModel>> = _playlistRemovingEvent

    private val _sharePlaylistEvent = MutableSharedFlow<SharePlaylistEvent>()
    val sharePlaylistEvent = _sharePlaylistEvent.asSharedFlow()

    private fun loadPlaylistContent(){
        viewModelScope.launch {
            playlistContentRepository.getTracksByIds(playlist.content.reversed()).collect { data ->
                _playlistContentState.postValue(data)
            }
        }
    }

    fun onTrackClicked(track: Track) {
        _trackNavigationEvent.value = Event(track)
    }

    fun onTrackToBeRemoved(track: Track) {
        _trackRemovingEvent.value = Event(track)
    }

    fun onPlaylistToBeRemoved(){
        _playlistRemovingEvent.value = Event(playlist)
    }

    fun onPlaylistInfoToBeChanged(){
        _playlistInfoNavigationEvent.value = Event(playlist)
    }

    fun onShareClicked(){
        viewModelScope.launch {
            if(_playlistContentState.value!!.isEmpty()) {
                _sharePlaylistEvent.emit(SharePlaylistEvent.NoContent("В этом плейлисте нет списка треков, которым можно поделиться"))
            }
            else{
                playlistContentRepository.getTracksByIds(playlist.content).collect { data ->
                    _sharePlaylistEvent.emit(SharePlaylistEvent.LetsDoIt(data))
                }
            }
        }
    }

    fun removeTrackFromPlaylist(track: Track){
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(playlist.id, track)
            loadPlaylistContent()
        }
    }

    fun removePlaylist(){
        viewModelScope.launch {
            playlistInteractor.removePlaylist(playlist)
        }
    }

    fun onViewPrepared(){
        loadPlaylistContent()
    }
}