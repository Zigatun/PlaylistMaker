package ussr.playlistmaker.main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ussr.playlistmaker.media.data.converters.DateTimeConverter
import ussr.playlistmaker.media.data.dao.FavoritesDao
import ussr.playlistmaker.media.data.entity.FavoriteTrackEntity
import ussr.playlistmaker.playlist.data.dao.PlaylistsDao
import ussr.playlistmaker.playlist.data.entity.PlaylistEntity

@Database(version = 4, entities = [FavoriteTrackEntity::class, PlaylistEntity::class])
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun playlistsDao(): PlaylistsDao
}