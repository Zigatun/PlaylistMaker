package ussr.playlistmaker.search.models

sealed interface TracksState {
    object Loading : TracksState
    data class Content(val tracks: List<Track>, val isHistory: Boolean) : TracksState
    data class Error(val message: String) : TracksState
    data class Empty(val message: String): TracksState
}