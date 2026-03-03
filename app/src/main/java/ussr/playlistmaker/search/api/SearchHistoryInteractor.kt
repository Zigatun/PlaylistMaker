package ussr.playlistmaker.search.api

import ussr.playlistmaker.search.models.Track

interface SearchHistoryInteractor {

    fun getHistory(consumer: SearchHistoryConsumer)

    fun addToHistory(track: Track)

    fun clearHistory()

    interface SearchHistoryConsumer {
        fun consume(history: ArrayDeque<Track>)
    }
}