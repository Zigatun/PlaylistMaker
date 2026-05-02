package ussr.playlistmaker.media.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ussr.playlistmaker.media.data.dao.FavoritesDao
import ussr.playlistmaker.media.data.entity.FavoriteTrackEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class])
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}