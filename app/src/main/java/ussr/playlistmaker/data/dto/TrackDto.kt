package ussr.playlistmaker.data.dto

import ussr.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

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

fun TrackDto.toModel() = Track(this.trackId,
    this.trackName,
    this.artistName,
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis),
    this.artworkUrl100,
    this.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
    this.previewUrl,
    this.collectionName,
    this.releaseDate?.atZone(ZoneId.systemDefault())?.year.toString(),
    this.primaryGenreName,
    this.country)