package ussr.playlistmaker.playlist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import ussr.playlistmaker.playlist.data.entity.PlaylistContentEntity

@Dao
interface PlaylistContentDao {
    @Insert(onConflict = IGNORE)
    suspend fun insertTrack(track: PlaylistContentEntity)

    @Delete
    suspend fun removeTrack(track: PlaylistContentEntity)

    @Query("DELETE FROM playlistContent WHERE trackId = :trackId")
    suspend fun removeTrackById(trackId: Long)

    @Query("DELETE FROM playlistContent")
    suspend fun removeTracks()

    @Query("SELECT * FROM playlistContent ORDER BY dateAdded DESC")
    suspend fun getTracks() : List<PlaylistContentEntity>
    @Query("SELECT * FROM playlistContent WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long) : PlaylistContentEntity

    @Query("SELECT * FROM playlistContent WHERE trackId IN (:trackIds)")
    suspend fun getTrackByIds(trackIds: List<Long>) : List<PlaylistContentEntity>

    @Query("SELECT trackId FROM playlistContent")
    suspend fun getTracksIds(): List<Long>

    @Query("SELECT COUNT(*) FROM playlistContent WHERE trackId = :trackId")
    suspend fun hasInStorage(trackId: Long) : Int
}