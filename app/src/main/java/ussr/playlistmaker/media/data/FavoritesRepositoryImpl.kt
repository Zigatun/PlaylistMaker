package ussr.playlistmaker.media.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ussr.playlistmaker.main.data.AppDatabase
import ussr.playlistmaker.media.data.mappers.toDatabaseEntity
import ussr.playlistmaker.media.data.mappers.toModel
import ussr.playlistmaker.media.domain.FavoritesRepository
import ussr.playlistmaker.search.models.Track

class FavoritesRepositoryImpl(private val database: AppDatabase) : FavoritesRepository {
    override suspend fun addTrack(track: Track) {
            database.favoritesDao().insertTrack(track.toDatabaseEntity())
    }

    override suspend fun removeTrack(track: Track) {
            database.favoritesDao().removeTrack(track.toDatabaseEntity())
    }

    override fun getFavorites(): Flow<List<Track>> = flow {
       emit(database.favoritesDao()
            .getTracks()
            .map { it.toModel() })
    }.flowOn(Dispatchers.IO)
}