package ussr.playlistmaker.media.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ussr.playlistmaker.media.data.mappers.toDatabaseEntity
import ussr.playlistmaker.media.data.mappers.toModel
import ussr.playlistmaker.media.domain.FavoritesRepository
import ussr.playlistmaker.search.models.Track

class FavoritesRepositoryImpl(private val database: FavoritesDatabase) : FavoritesRepository {
    override fun addTrack(track: Track) {
        database.favoritesDao().insertTrack(track.toDatabaseEntity())
    }

    override fun removeTrack(track: Track) {
        database.favoritesDao().removeTrack(track.toDatabaseEntity())
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
        val tracks = database.favoritesDao().getTracks()
        emit(tracks.map { it.toModel() })
    }
}