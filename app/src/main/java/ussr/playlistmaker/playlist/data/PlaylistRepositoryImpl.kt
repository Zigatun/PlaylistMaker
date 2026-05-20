package ussr.playlistmaker.playlist.data

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ussr.playlistmaker.main.data.AppDatabase
import ussr.playlistmaker.playlist.data.mappers.toDatabaseEntity
import ussr.playlistmaker.playlist.data.mappers.toModel
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.domain.PlaylistRepository

class PlaylistRepositoryImpl(private val database: AppDatabase, private val gson: Gson): PlaylistRepository {
    override suspend fun CreatePlaylist(playlist: PlaylistModel) {
        database.playlistsDao().insertPlaylist(playlist.toDatabaseEntity(gson))
    }

    override suspend fun RemovePlaylist(playlist: PlaylistModel) {
        database.playlistsDao().removePlaylist(playlist.toDatabaseEntity(gson))
    }

    override suspend fun UpdatePlaylist(playlist: PlaylistModel) {
        val db = playlist.toDatabaseEntity(gson)
        database.playlistsDao().updatePlaylist(db)
    }

    override suspend fun GetPlaylists(): Flow<List<PlaylistModel>> = flow {
        emit(database.playlistsDao()
            .getPlaylists()
            .map { it.toModel(gson) })
    }.flowOn(Dispatchers.IO)

    override suspend fun GetPlaylist(playlistId: Long): PlaylistModel {
        return database.playlistsDao()
            .getPlaylistById(playlistId)
            .toModel(gson)
    }

}