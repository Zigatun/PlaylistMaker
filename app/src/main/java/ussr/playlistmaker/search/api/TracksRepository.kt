package ussr.playlistmaker.search.api

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.util.Resource

interface TracksRepository {
    fun searchTracks(searchPattern: String): Flow<Resource<List<Track>>>
}