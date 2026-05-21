package ussr.playlistmaker.playlist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "playlistContent")
data class PlaylistContentEntity(
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