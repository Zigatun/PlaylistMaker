package ussr.playlistmaker.playlist.data

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

    override suspend fun hasTrack(trackId: Long): Boolean {
        return database.playlistsContentDao().hasInStorage(trackId) > 0
    }

    override suspend fun removeTrack(track: Track) {
        database.playlistsContentDao().removeTrack(track.toDatabaseEntity())
    }
}