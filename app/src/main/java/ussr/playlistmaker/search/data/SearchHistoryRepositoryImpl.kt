package ussr.playlistmaker.search.data

import com.google.gson.Gson
import ussr.playlistmaker.search.data.dto.TrackDto
import ussr.playlistmaker.search.data.mappers.toDto
import ussr.playlistmaker.search.data.mappers.toModel
import ussr.playlistmaker.search.api.SearchHistoryRepository
import ussr.playlistmaker.search.models.Track

private const val PLAYLISTMAKER_SEARCH_HISTORY = "playlistmaker_search_history"
private const val PLAYLISTMAKER_HISTORY_LIMIT = 10

class SearchHistoryRepositoryImpl(val gson: Gson, private val storage: StorageService) : SearchHistoryRepository {
    private fun flush(tracks: ArrayDeque<Track>) {
        val dtos = tracks.map { it.toDto() }
        val json = gson.toJson(dtos)
        storage.put(PLAYLISTMAKER_SEARCH_HISTORY, json)
    }
    override fun get(): ArrayDeque<Track> {
        val json = storage.get(PLAYLISTMAKER_SEARCH_HISTORY)
        if(json.isBlank()) return ArrayDeque()

        val dtos = gson.fromJson(json, Array<TrackDto>::class.java) ?: emptyArray()
        return dtos.map { it.toModel() }.toCollection(ArrayDeque())
    }

    override fun add(track: Track) {
        val history = get()

        history.removeIf { it.trackId == track.trackId }
        history.addFirst(track)

        if (history.size > PLAYLISTMAKER_HISTORY_LIMIT)
            history.removeLast()

        flush(history)
    }

    override fun clear() {
        storage.put(PLAYLISTMAKER_SEARCH_HISTORY, "[]")
    }
}