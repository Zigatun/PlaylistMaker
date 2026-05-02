package ussr.playlistmaker.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ussr.playlistmaker.search.api.TracksInteractor
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.util.Resource

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    override fun searchTracks(
        expression: String
    ) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}