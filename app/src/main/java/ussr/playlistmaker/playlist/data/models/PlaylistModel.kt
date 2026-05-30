package ussr.playlistmaker.playlist.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistModel(
    val id: Long = 0,
    var title: String,
    var description: String = "",
    var imagePath: String? = null,
    val content: MutableList<Long>) : Parcelable {
    fun formatTrackCount(): String = when {
        content.isEmpty() -> "Плейлист пуст"
        content.size % 100 in 11..14 -> "${content.size} треков"
        content.size % 10 == 1 -> "${content.size} трек"
        content.size % 10 in 2..4 -> "${content.size} трека"
        else ->  "${content.size} треков"
    }
}