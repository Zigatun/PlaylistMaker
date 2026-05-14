package ussr.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ussr.playlistmaker.media.domain.FavoritesInteractor
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.models.TracksState
import ussr.playlistmaker.search.ui.Event

class FavoritesFragmentViewModel(private val favoritesInteractor: FavoritesInteractor): ViewModel() {

    private val trackViewState = MutableLiveData<TracksState>(TracksState.Loading)
    fun observableTrackViewState(): LiveData<TracksState> = trackViewState
    private val trackNavigationEvent = MutableLiveData<Event<Track>>()
    fun observableTrackNavigationEvent(): LiveData<Event<Track>> = trackNavigationEvent

    fun onTrackClicked(track: Track){
        trackNavigationEvent.value = Event(track)
    }

    private fun loadFavorites(){
        viewModelScope.launch {
            trackViewState.value = TracksState.Loading
            favoritesInteractor.getFavorites().collect { data ->
                if(data.isEmpty()){
                    trackViewState.postValue(TracksState.Empty("Ваша медиатека пуста"))
                }
                else{
                    trackViewState.postValue(TracksState.Content(data, false))
                }
            }
        }
    }

    fun onViewPrepared(){
        loadFavorites()
    }
}