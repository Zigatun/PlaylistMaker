package ussr.playlistmaker.playlist.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.search.models.Track

interface PlaylistContentRepository {
    suspend fun addTrack(track: Track)
    suspend fun getTrack(trackId: Long): Track
    suspend fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>>
    suspend fun hasTrack(trackId: Long): Boolean
    suspend fun removeTrack(track: Track)
    suspend fun removeTrackById(trackId: Long)
    suspend fun removeTracks()
}