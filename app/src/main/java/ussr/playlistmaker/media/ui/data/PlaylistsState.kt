package ussr.playlistmaker.media.ui.data

import ussr.playlistmaker.playlist.data.models.PlaylistModel

sealed interface PlaylistsState {
    object Loading : PlaylistsState
    data class Content(val playlists: List<PlaylistModel>) : PlaylistsState
    data class Empty(val message: String): PlaylistsState
}