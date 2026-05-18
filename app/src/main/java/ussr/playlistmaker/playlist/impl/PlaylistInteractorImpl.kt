package ussr.playlistmaker.playlist.impl

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.domain.PlaylistInteractor
import ussr.playlistmaker.playlist.domain.PlaylistRepository

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override suspend fun CreatePlaylist(playlist: PlaylistModel) {
        playlistRepository.CreatePlaylist(playlist)
    }

    override suspend fun RemovePlaylist(playlist: PlaylistModel) {
        playlistRepository.RemovePlaylist(playlist)
    }

    override suspend fun GetPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.GetPlaylists()
    }
}