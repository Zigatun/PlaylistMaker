package ussr.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ussr.playlistmaker.search.api.SearchHistoryInteractor
import ussr.playlistmaker.search.api.TracksInteractor
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.models.TracksState
import ussr.playlistmaker.search.ui.Event

class SearchActivityViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val trackViewState = MutableLiveData<TracksState>()
    fun observableTrackViewState(): LiveData<TracksState> = trackViewState
    private val clearTextIsAvailable = MutableLiveData<Boolean>()
    fun observableClearTextIsAvailable(): LiveData<Boolean> = clearTextIsAvailable
    private val searchBarText = MutableLiveData<String>()
    fun observableSearchBarText(): LiveData<String> = searchBarText

    private val trackNavigationEvent = MutableLiveData<Event<Track>>()
    fun observableTrackNavigationEvent(): LiveData<Event<Track>> = trackNavigationEvent

    private var currentText: String = ""
    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun onTrackClicked(track: Track) {
        searchHistoryInteractor.addToHistory(track)
        val state = trackViewState.value
        if(state is TracksState.Content && !state.isHistory)
        {
            trackNavigationEvent.value = Event(track)
        }else{
            loadHistory()
        }

    }

    fun onClearClicked() {
        currentText = ""
        searchBarText.postValue("")
        trackViewState.postValue(TracksState.Empty(""))
    }
    fun onHistoryClearClicked() {
        searchHistoryInteractor.clearHistory()
        loadHistory()
    }

    fun onSearchSubmitted(text: String) {
        doSearch(text)
    }

    fun onSearchTextChanged(text: String) {
        currentText = text
        searchBarText.postValue(text)
        if (text.isBlank()) {
            clearTextIsAvailable.postValue(false)
            loadHistory()
        } else {
            clearTextIsAvailable.postValue(true)
            searchDebounce(text)
        }
    }

    fun onSearchFocusChanged(hasFocus: Boolean) {
        if (hasFocus && currentText.isBlank()) {
            loadHistory()
        }
    }

    private fun doSearch(request: String) {
        trackViewState.value = TracksState.Loading

        tracksInteractor.searchTracks(request, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                if(errorMessage != null){
                    trackViewState.postValue(
                        TracksState.Error(errorMessage)
                    )
                    return
                }
                if (foundTracks != null && foundTracks.isEmpty()) {
                    trackViewState.postValue(
                        TracksState.Empty("Ничего не найдено")
                    )
                } else {
                    trackViewState.postValue(
                        TracksState.Content(foundTracks.orEmpty(), false)
                    )
                }
            }
        })
    }

    private fun loadHistory() {
        searchHistoryInteractor.getHistory(object : SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(history: ArrayDeque<Track>) {
                if (history.isEmpty()) {
                    trackViewState.postValue(TracksState.Empty(""))
                } else {
                    trackViewState.postValue(TracksState.Content(history.toList(), true))
                }
            }
        })
    }

    private fun searchDebounce(text: String) {
        if(text == latestSearchText){
            return
        }
        latestSearchText = text

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCHBAR_DEBOUNCE_DELAY)
            doSearch(text)
        }
    }

    companion object {
        const val SEARCHBAR_DEBOUNCE_DELAY = 2000L
    }
}