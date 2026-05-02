package ussr.playlistmaker.search.api

import kotlinx.coroutines.flow.Flow
import ussr.playlistmaker.search.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>
}