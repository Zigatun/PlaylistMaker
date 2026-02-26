package ussr.playlistmaker.search.api

import ussr.playlistmaker.search.models.Track

interface TracksRepository {
    fun searchTracks(searchPattern: String): List<Track>
}