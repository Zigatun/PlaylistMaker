package ussr.playlistmaker.data.dto

import java.time.Instant

data class TrackDto (val trackId: Long,
                     val trackName: String,
                     val artistName: String,
                     val trackTimeMillis: Long,
                     val artworkUrl100: String,
                     val previewUrl: String,
                     val collectionName: String?,
                     val releaseDate: Instant?,
                     val primaryGenreName:String,
                     val country:String)