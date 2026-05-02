package ussr.playlistmaker.media.data.mappers

import ussr.playlistmaker.media.data.entity.FavoriteTrackEntity
import ussr.playlistmaker.search.models.Track
import java.time.LocalDateTime

fun FavoriteTrackEntity.toModel() = Track(this.trackId,
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

fun Track.toDatabaseEntity() = FavoriteTrackEntity(
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