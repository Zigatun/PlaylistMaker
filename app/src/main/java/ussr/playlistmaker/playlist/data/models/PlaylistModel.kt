package ussr.playlistmaker.playlist.data.models

import ussr.playlistmaker.search.models.Track
import java.time.LocalDateTime

data class PlaylistModel(
    val id: Long = 0,
    val title: String,
                         val description: String = "",
                         val imagePath: String? = null,
                         val content: List<Track>)