package ussr.playlistmaker.playlist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ussr.playlistmaker.playlist.data.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Query("SELECT * FROM playlists ORDER BY dateAdded DESC")
    suspend fun getPlaylists() : List<PlaylistEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun removePlaylist(playlist: PlaylistEntity)
}