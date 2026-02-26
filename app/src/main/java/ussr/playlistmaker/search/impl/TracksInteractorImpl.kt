package ussr.playlistmaker.search.impl

import ussr.playlistmaker.search.api.TracksInteractor
import ussr.playlistmaker.search.api.TracksRepository

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    override fun searchTracks(
        expression: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}