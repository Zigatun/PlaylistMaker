package ussr.playlistmaker.media.impl

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.media.domain.FavoritesInteractor
import ussr.playlistmaker.media.domain.FavoritesRepository
import ussr.playlistmaker.search.models.Track

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        favoritesRepository.removeTrack(track)
    }


    override fun getFavorites(): Flow<List<Track>> {
        return favoritesRepository.getFavorites()
    }
}