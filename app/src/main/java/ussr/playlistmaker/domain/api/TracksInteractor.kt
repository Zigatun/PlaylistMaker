package ussr.playlistmaker.domain.api

import ussr.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks:List<Track>)
    }
}