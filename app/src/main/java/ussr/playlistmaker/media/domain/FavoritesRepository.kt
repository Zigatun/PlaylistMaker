package ussr.playlistmaker.media.domain

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.search.models.Track

interface FavoritesRepository {
    fun addTrack(track: Track)
    fun removeTrack(track: Track)
    fun getFavorites() : Flow<List<Track>>
}