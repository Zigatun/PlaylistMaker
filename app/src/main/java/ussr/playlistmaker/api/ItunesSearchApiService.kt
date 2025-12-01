package ussr.playlistmaker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ussr.playlistmaker.models.ItunesSearchResult

interface ItunesSearchApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<ItunesSearchResult>
}