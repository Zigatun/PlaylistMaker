package ussr.playlistmaker.domain.api

import ussr.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun get(): ArrayDeque<Track>
    fun add(track: Track)
    fun clear()
}