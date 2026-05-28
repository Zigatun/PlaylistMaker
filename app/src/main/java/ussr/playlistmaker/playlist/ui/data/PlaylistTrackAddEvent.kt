package ussr.playlistmaker.playlist.ui.data

sealed interface PlaylistTrackAddEvent {
    data class TrackAdded(val message: String) : PlaylistTrackAddEvent
    data class TrackAlreadyExists(val message: String) : PlaylistTrackAddEvent
}