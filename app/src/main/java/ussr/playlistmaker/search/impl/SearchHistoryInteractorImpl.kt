package ussr.playlistmaker.search.impl

import ussr.playlistmaker.search.api.SearchHistoryInteractor
import ussr.playlistmaker.search.api.SearchHistoryRepository
import ussr.playlistmaker.search.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository): SearchHistoryInteractor {
    override fun getHistory(consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
            consumer.consume(repository.get())
    }

    override fun addToHistory(track: Track) {
        repository.add(track)
    }

    override fun clearHistory() {
        repository.clear()
    }
}