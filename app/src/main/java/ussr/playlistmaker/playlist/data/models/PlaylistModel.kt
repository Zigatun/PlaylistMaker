package ussr.playlistmaker.playlist.data.models

data class PlaylistModel(
    val id: Long = 0,
    val title: String,
                         val description: String = "",
                         val imagePath: String? = null,
                         val content: MutableList<Long>)
{
    fun formatTrackCount(): String = when {
        content.isEmpty() -> "Плейлист пуст"
        content.size % 100 in 11..14 -> "${content.size} треков"
        content.size % 10 == 1 -> "${content.size} трек"
        content.size % 10 in 2..4 -> "${content.size} трека"
        else ->  "${content.size} треков"
    }
}