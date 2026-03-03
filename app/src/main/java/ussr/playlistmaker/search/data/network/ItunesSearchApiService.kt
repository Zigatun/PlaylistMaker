package ussr.playlistmaker.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ussr.playlistmaker.search.data.dto.ItunesSearchResponse

interface ItunesSearchApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<ItunesSearchResponse>
}