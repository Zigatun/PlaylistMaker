package ussr.playlistmaker.search.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
class Track (val trackId: Long,
             val trackName: String,
             val artistName: String,
             val trackTime: String,
             val artworkUrl: String,
             val coverArtworkUrl: String,
             val previewUrl: String,
             val collectionName: String?,
             val releaseDate: Instant?,
             val yearOfRelease: String?,
             val genreName:String,
             val country:String) : Parcelable