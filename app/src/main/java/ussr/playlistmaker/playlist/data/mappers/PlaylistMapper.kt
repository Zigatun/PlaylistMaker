package ussr.playlistmaker.playlist.data.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ussr.playlistmaker.playlist.data.entity.PlaylistEntity
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.search.models.Track
import java.time.LocalDateTime

fun PlaylistModel.toDatabaseEntity(gson: Gson) = PlaylistEntity(
    title = this.title,
    description = this.description,
    content = gson.toJson(this.content),
    dateAdded = LocalDateTime.now(),
    imagePath = this.imagePath)

fun PlaylistEntity.toModel(gson: Gson): PlaylistModel{
    val type = object : TypeToken<List<Track>>() {}.type
    return PlaylistModel(
        id = this.id,
        title = this.title,
        description = this.description,
        imagePath = this.imagePath,
        content = gson.fromJson(this.content, type))
}
