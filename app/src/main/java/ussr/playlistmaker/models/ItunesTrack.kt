package ussr.playlistmaker.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class ItunesTrack (val trackId: Long,
                        val trackName: String,
                        val artistName: String,
                        val trackTimeMillis: Long,
                        val artworkUrl100: String,
                        val collectionName: String,
                        val releaseDate:Int,
                        val primaryGenreName:String,
                        val country:String): Parcelable{
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getHumanizedTime() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}