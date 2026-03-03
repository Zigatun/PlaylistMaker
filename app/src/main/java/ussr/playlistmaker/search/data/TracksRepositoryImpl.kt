package ussr.playlistmaker.search.data

import ussr.playlistmaker.search.data.dto.ItunesSearchRequest
import ussr.playlistmaker.search.data.dto.ItunesSearchResponse
import ussr.playlistmaker.search.data.mappers.toModel
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(searchPattern: String): Resource<List<Track>> {
        val response = networkClient.doRequest(ItunesSearchRequest(searchPattern))
        return when (response.resultCode){
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 ->{
                Resource.Success((response as ItunesSearchResponse).results.map {
                    it.toModel()
                })
            }

            else -> {
                Resource.Error("Внутренняя ошибка сервера")
            }
        }
    }
}