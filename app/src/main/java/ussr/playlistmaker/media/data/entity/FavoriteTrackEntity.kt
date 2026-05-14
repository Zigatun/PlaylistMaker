package ussr.playlistmaker.media.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime

@Entity(tableName = "favorites")
data class FavoriteTrackEntity(
                               @PrimaryKey val trackId: Long,
                               val artworkUrl: String,
                               val coverArtworkUrl: String,
                               val trackName: String,
                               val artistName: String,
                               val collectionName: String?,
                               val releaseDate: String?,
                               val genreName:String,
                               val country:String,
                               val trackTime: String,
                               val previewUrl: String,
                                val dateAdded: LocalDateTime
)