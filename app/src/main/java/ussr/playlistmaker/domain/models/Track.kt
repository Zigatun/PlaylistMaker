package ussr.playlistmaker.domain.models

import ussr.playlistmaker.data.dto.TrackDto
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Locale

class Track (val trackId: Long,
                     val trackName: String,
                     val artistName: String,
                     val trackTime: String,
                     val artworkUrl: String,
                     val previewUrl: String,
                     val collectionName: String?,
                     val yearOfRelease: String?,
                     val genreName:String,
                     val country:String)
