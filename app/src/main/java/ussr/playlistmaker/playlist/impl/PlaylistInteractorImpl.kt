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
        playlistRepository.getPlaylists().collect { p ->
            if(p.isEmpty()){
                playlistContentRepository.removeTracks()
            }
            else {
                playlist.content.forEach {
                    val playlists = p.filter { pl -> pl.content.contains(it) }
                    if(playlists.isEmpty()){
                        playlistContentRepository.removeTrackById(it)
                    }
                }
            }
        }

    }

    override suspend fun modifyPlaylist(playlist: PlaylistModel) {
        playlistRepository.updatePlaylist(playlist)
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

    override suspend fun removeTrackFromPlaylist(
        playlistId: Long,
        track: Track
    ) {
        val playlist = playlistRepository.getPlaylist(playlistId)
        playlist.content.remove(track.trackId)
        playlistRepository.updatePlaylist(playlist)
        playlistRepository.getPlaylists().collect { p ->
            val playlists = p.filter { pl -> pl.content.contains(track.trackId) }
            if(playlists.isEmpty()){
                playlistContentRepository.removeTrack(track)
            }
        }
    }
}