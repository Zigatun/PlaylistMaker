package ussr.playlistmaker.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ussr.playlistmaker.search.data.dto.ItunesSearchResponse

interface ItunesSearchApiService {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") term: String): ItunesSearchResponse
}