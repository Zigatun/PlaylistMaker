package ussr.playlistmaker.playlist.impl

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.domain.PlaylistContentRepository
import ussr.playlistmaker.playlist.domain.PlaylistInteractor
import ussr.playlistmaker.playlist.domain.PlaylistRepository
import ussr.playlistmaker.playlist.ui.data.PlaylistTrackAddEvent
import ussr.playlistmaker.search.models.Track

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository, private val playlistContentRepository: PlaylistContentRepository): PlaylistInteractor {
    override suspend fun createPlaylist(playlist: PlaylistModel) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun removePlaylist(playlist: PlaylistModel) {
        playlistRepository.removePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun putTrackIntoPlaylist(
        playlistId: Long,
        track: Track
    ): PlaylistTrackAddEvent {
        val playlist = playlistRepository.getPlaylist(playlistId)
        if(playlist.content.any { it == track.trackId })
            return PlaylistTrackAddEvent.TrackAlreadyExists("Трек уже добавлен в плейлист ${playlist.title}")
        playlist.content.add(track.trackId)
        playlistContentRepository.addTrack(track)
        playlistRepository.updatePlaylist(playlist)

        return PlaylistTrackAddEvent.TrackAdded("Добавлено в плейлист ${playlist.title}")
    }
}