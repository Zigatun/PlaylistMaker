package ussr.playlistmaker.search.impl

import ussr.playlistmaker.search.api.TracksInteractor
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.util.Resource

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    override fun searchTracks(
        expression: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        val t = Thread {
            when(val r = repository.searchTracks(expression)) {
                is Resource.Error -> {
                    consumer.consume(null, r.message)
                }
                is Resource.Success -> {
                    consumer.consume(r.data, null)
                }
            }
        }
        t.start()
    }
}