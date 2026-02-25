package ussr.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ussr.playlistmaker.data.dto.ItunesSearchResponse

interface ItunesSearchApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<ItunesSearchResponse>
}