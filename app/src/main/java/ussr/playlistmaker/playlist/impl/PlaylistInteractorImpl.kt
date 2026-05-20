package ussr.playlistmaker.playlist.impl

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.domain.PlaylistInteractor
import ussr.playlistmaker.playlist.domain.PlaylistRepository
import ussr.playlistmaker.playlist.ui.data.PlaylistTrackAddEvent
import ussr.playlistmaker.search.models.Track

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

    override suspend fun PutTrackIntoPlaylist(
        playlistId: Long,
        track: Track
    ): PlaylistTrackAddEvent {
        val playlist = playlistRepository.GetPlaylist(playlistId)
        if(playlist.content.any { it.trackId == track.trackId })
            return PlaylistTrackAddEvent.TrackAlreadyExists("Трек уже добавлен в плейлист ${playlist.title}")
        playlist.content.add(track)
        playlistRepository.UpdatePlaylist(playlist)
        return PlaylistTrackAddEvent.TrackAdded("Добавлено в плейлист ${playlist.title}")
    }
}