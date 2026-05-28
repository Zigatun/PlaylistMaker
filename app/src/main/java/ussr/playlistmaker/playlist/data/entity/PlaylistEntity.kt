package ussr.playlistmaker.playlist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "playlists")
data class PlaylistEntity(
                          @PrimaryKey(autoGenerate = true)
                          val id: Long = 0,
                          val title: String,
                          val description: String = "",
                          val imagePath: String?,
                          val content: String,
                          val dateAdded: LocalDateTime)