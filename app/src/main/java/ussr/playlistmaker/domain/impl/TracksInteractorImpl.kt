package ussr.playlistmaker.domain.impl

import ussr.playlistmaker.domain.api.TracksInteractor
import ussr.playlistmaker.domain.api.TracksRepository

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