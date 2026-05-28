package ussr.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: PlaylistModel)
    suspend fun removePlaylist(playlist: PlaylistModel)
    suspend fun updatePlaylist(playlist: PlaylistModel)
    suspend fun getPlaylists() : Flow<List<PlaylistModel>>
    suspend fun getPlaylist(playlistId: Long): PlaylistModel
}