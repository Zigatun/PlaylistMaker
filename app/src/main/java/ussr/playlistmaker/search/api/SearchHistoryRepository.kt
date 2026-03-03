package ussr.playlistmaker.search.api

import ussr.playlistmaker.search.models.Track

interface SearchHistoryRepository {
    fun get(): ArrayDeque<Track>
    fun add(track: Track)
    fun clear()
}