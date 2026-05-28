package ussr.playlistmaker.playlist.domain

import ussr.playlistmaker.search.models.Track

interface PlaylistContentRepository {
    suspend fun addTrack(track: Track)
    suspend fun getTrack(trackId: Long): Track
    suspend fun hasTrack(trackId: Long): Boolean
    suspend fun removeTrack(track: Track)
}