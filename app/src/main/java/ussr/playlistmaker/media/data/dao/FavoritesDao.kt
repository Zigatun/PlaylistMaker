package ussr.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.media.data.entity.FavoriteTrackEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = REPLACE)
    fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    fun removeTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorites")
    fun getTracks() : List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorites")
    fun getTracksIds(): List<Long>

    @Query("SELECT COUNT(*) FROM favorites WHERE trackId = :trackId")
    fun hasInStorage(trackId: Long) : Int
}