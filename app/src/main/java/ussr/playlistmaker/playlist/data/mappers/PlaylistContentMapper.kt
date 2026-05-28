package ussr.playlistmaker.playlist.data.mappers

import ussr.playlistmaker.playlist.data.entity.PlaylistContentEntity
import ussr.playlistmaker.search.models.Track
import java.time.LocalDateTime

fun PlaylistContentEntity.toModel() = Track(this.trackId,
    this.trackName,
    this.artistName,
    this.trackTime,
    this.artworkUrl,
    this.coverArtworkUrl,
    this.previewUrl,
    this.collectionName,
    null,
    this.releaseDate,
    this.genreName,
    this.country, true)

fun Track.toDatabaseEntity() = PlaylistContentEntity(
    this.trackId,
    this.artworkUrl,
    this.coverArtworkUrl,
    this.trackName,
    this.artistName,
    this.collectionName,
    null,
    this.genreName,
    this.country,
    this.trackTime,
    this.previewUrl,
    LocalDateTime.now()
)