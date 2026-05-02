package ussr.playlistmaker.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ussr.playlistmaker.search.data.dto.ItunesSearchRequest
import ussr.playlistmaker.search.data.dto.ItunesSearchResponse
import ussr.playlistmaker.search.data.mappers.toModel
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(searchPattern: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(ItunesSearchRequest(searchPattern))
        when (response.resultCode){
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 ->{
                with(response as ItunesSearchResponse){
                    emit(Resource.Success(response.results.map {
                        it.toModel()
                    }))
                }
            }
            else -> {
                emit(Resource.Error("Внутренняя ошибка сервера"))
            }
        }
    }
}