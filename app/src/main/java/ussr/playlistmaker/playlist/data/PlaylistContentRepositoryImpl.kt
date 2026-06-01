package ussr.playlistmaker.playlist.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ussr.playlistmaker.main.data.AppDatabase
import ussr.playlistmaker.playlist.data.mappers.toDatabaseEntity
import ussr.playlistmaker.playlist.data.mappers.toModel
import ussr.playlistmaker.playlist.domain.PlaylistContentRepository
import ussr.playlistmaker.search.models.Track

class PlaylistContentRepositoryImpl(private val database: AppDatabase): PlaylistContentRepository {
    override suspend fun addTrack(track: Track) {
        database.playlistsContentDao().insertTrack(track.toDatabaseEntity())
    }

    override suspend fun getTrack(trackId: Long):Track {
        return database.playlistsContentDao().getTrackById(trackId).toModel()
    }

    override suspend fun getTracksByIds(trackIds: List<Long>): Flow<List<Track>> = flow {
        val tracks = database.playlistsContentDao()
            .getTrackByIds(trackIds)
            .map { it.toModel() }

        val tracksById = tracks.associateBy { it.trackId }

        emit(trackIds.mapNotNull { tracksById[it] })
    }.flowOn(Dispatchers.IO)

    override suspend fun hasTrack(trackId: Long): Boolean {
        return database.playlistsContentDao().hasInStorage(trackId) > 0
    }

    override suspend fun removeTrack(track: Track) {
        database.playlistsContentDao().removeTrack(track.toDatabaseEntity())
    }

    override suspend fun removeTrackById(trackId: Long) {
        database.playlistsContentDao().removeTrackById(trackId)
    }

    override suspend fun removeTracks() {
        database.playlistsContentDao().removeTracks()
    }
}