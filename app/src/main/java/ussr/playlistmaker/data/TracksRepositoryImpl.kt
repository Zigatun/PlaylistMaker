package ussr.playlistmaker.data

import ussr.playlistmaker.data.dto.ItunesSearchRequest
import ussr.playlistmaker.data.dto.ItunesSearchResponse
import ussr.playlistmaker.data.mappers.toModel
import ussr.playlistmaker.domain.api.TracksRepository
import ussr.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(searchPattern: String): List<Track> {
        val response = networkClient.doRequest(ItunesSearchRequest(searchPattern))

        if(response.resultCode == 200){
            return (response as ItunesSearchResponse).results.map {
                it.toModel()
            }
        } else {
            return emptyList()
        }
    }
}