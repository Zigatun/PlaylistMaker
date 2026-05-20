package ussr.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.ui.data.PlaylistTrackAddEvent

import ussr.playlistmaker.search.models.Track

interface PlaylistInteractor {
    suspend fun CreatePlaylist(playlist: PlaylistModel)
    suspend fun RemovePlaylist(playlist: PlaylistModel)
    suspend fun GetPlaylists() : Flow<List<PlaylistModel>>

    suspend fun PutTrackIntoPlaylist(playlistId: Long, track: Track) : PlaylistTrackAddEvent
}