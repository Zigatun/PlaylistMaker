package ussr.playlistmaker.playlist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import ussr.playlistmaker.playlist.data.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Query("SELECT * FROM playlists ORDER BY dateAdded DESC")
    suspend fun getPlaylists() : List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long) : PlaylistEntity

    @Insert(onConflict = REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun removePlaylist(playlist: PlaylistEntity)
}