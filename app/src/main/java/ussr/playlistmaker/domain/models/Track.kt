package ussr.playlistmaker.domain.models

class Track (val trackId: Long,
                     val trackName: String,
                     val artistName: String,
                     val trackTime: String,
                     val artworkUrl: String,
                     val coverArtworkUrl: String,
                     val previewUrl: String,
                     val collectionName: String?,
                     val yearOfRelease: String?,
                     val genreName:String,
                     val country:String)
