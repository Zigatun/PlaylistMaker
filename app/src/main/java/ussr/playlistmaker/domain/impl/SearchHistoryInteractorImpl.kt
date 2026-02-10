package ussr.playlistmaker.domain.impl

import ussr.playlistmaker.domain.api.SearchHistoryInteractor
import ussr.playlistmaker.domain.api.SearchHistoryRepository
import ussr.playlistmaker.domain.models.Track

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