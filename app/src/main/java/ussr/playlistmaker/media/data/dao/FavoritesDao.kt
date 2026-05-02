package ussr.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ussr.playlistmaker.media.data.entity.FavoriteTrackEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun removeTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorites")
    suspend fun getTracks() : List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorites")
    suspend fun getTracksIds(): List<Long>

    @Query("SELECT COUNT(*) FROM favorites WHERE trackId = :trackId")
    suspend fun hasInStorage(trackId: Long) : Int
}