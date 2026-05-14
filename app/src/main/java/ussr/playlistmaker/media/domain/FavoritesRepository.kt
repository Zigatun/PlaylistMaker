package ussr.playlistmaker.media.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.search.models.Track

interface FavoritesRepository {
    suspend fun addTrack(track: Track)
    suspend fun removeTrack(track: Track)
    fun getFavorites() : Flow<List<Track>>
}