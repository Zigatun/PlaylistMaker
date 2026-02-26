package ussr.playlistmaker.search.impl

import ussr.playlistmaker.search.api.SearchHistoryInteractor
import ussr.playlistmaker.search.api.SearchHistoryRepository
import ussr.playlistmaker.search.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository): SearchHistoryInteractor {
    override fun getHistory(consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
        val t = Thread {
            consumer.consume(repository.get())
        }
        t.start()
    }

    override fun addToHistory(track: Track) {
        val t = Thread {
            repository.add(track)
        }
        t.start()
    }

    override fun clearHistory() {
        val t = Thread {
            repository.clear()
        }
        t.start()
    }
}