package ussr.playlistmaker.playlist.ui.data

sealed interface CreatePlaylistEvent {
    data class PlaylistCreated(val playlistTitle: String) : CreatePlaylistEvent
    data class PlaylistModified(val playlistTitle: String) : CreatePlaylistEvent
}