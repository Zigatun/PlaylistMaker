package ussr.playlistmaker.media.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ussr.playlistmaker.media.data.converters.DateTimeConverter
import ussr.playlistmaker.media.data.dao.FavoritesDao
import ussr.playlistmaker.media.data.entity.FavoriteTrackEntity

@Database(version = 2, entities = [FavoriteTrackEntity::class])
@TypeConverters(DateTimeConverter::class)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}