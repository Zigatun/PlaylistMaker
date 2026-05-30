package ussr.playlistmaker.playlist.ui.data

import ussr.playlistmaker.search.models.Track

sealed interface SharePlaylistEvent {
    data class NoContent(val message: String) : SharePlaylistEvent
    data class LetsDoIt(val payload: List<Track>) : SharePlaylistEvent
}