package ussr.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel

interface PlaylistInteractor {
    suspend fun CreatePlaylist(playlist: PlaylistModel)
    suspend fun RemovePlaylist(playlist: PlaylistModel)
    suspend fun GetPlaylists() : Flow<List<PlaylistModel>>
}