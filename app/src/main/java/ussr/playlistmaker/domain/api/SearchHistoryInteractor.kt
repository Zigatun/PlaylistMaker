package ussr.playlistmaker.domain.api

import ussr.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {

    fun getHistory(consumer: SearchHistoryConsumer)

    fun addToHistory(track: Track)

    fun clearHistory()

    interface SearchHistoryConsumer {
        fun consume(history: ArrayDeque<Track>)
    }
}