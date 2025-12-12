package ussr.playlistmaker.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ItunesTrack (val trackId: Long, val trackName: String, val artistName: String, val trackTimeMillis: Long, val artworkUrl100: String): Parcelable