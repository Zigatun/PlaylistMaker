package ussr.playlistmaker.search.api

import ussr.playlistmaker.search.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks:List<Track>)
    }
}