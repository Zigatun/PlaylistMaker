package ussr.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.ui.data.PlaylistTrackAddEvent

import ussr.playlistmaker.search.models.Track

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: PlaylistModel)
    suspend fun removePlaylist(playlist: PlaylistModel)
    suspend fun modifyPlaylist(playlist: PlaylistModel)
    suspend fun getPlaylists() : Flow<List<PlaylistModel>>

    suspend fun putTrackIntoPlaylist(playlistId: Long, track: Track) : PlaylistTrackAddEvent
    suspend fun removeTrackFromPlaylist(playlistId: Long, track: Track)
}