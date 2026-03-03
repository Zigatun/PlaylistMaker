package ussr.playlistmaker.search.api

import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.util.Resource

interface TracksRepository {
    fun searchTracks(searchPattern: String): Resource<List<Track>>
}