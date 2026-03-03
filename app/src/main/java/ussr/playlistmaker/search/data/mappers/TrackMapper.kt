package ussr.playlistmaker.search.data.mappers

import ussr.playlistmaker.search.data.dto.TrackDto
import ussr.playlistmaker.search.models.Track
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun mmssToMillisDuration(mmss: String): Long {
    val parts = mmss.split(":")
    if (parts.size != 2) {
        throw IllegalArgumentException("Invalid MM:SS format")
    }

    val minutes = parts[0].toLong()
    val seconds = parts[1].toLong()

    val duration: Duration = minutes.minutes + seconds.seconds
    return duration.inWholeMilliseconds
}

fun TrackDto.toModel() = Track(this.trackId,
    this.trackName,
    this.artistName,
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis),
    this.artworkUrl100,
    this.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
    this.previewUrl,
    this.collectionName,
    this.releaseDate,
    this.releaseDate?.atZone(ZoneId.systemDefault())?.year.toString(),
    this.primaryGenreName,
    this.country)

fun Track.toDto(): TrackDto = TrackDto(
    this.trackId,
    this.trackName,
    this.artistName,
    mmssToMillisDuration(this.trackTime),
    this.artworkUrl,
    previewUrl = this.previewUrl,
    collectionName = this.collectionName,
    releaseDate = this.releaseDate,
    primaryGenreName = this.genreName,
    country = this.country
)