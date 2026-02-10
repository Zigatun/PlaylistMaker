package ussr.playlistmaker.data.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ussr.playlistmaker.data.NetworkClient
import ussr.playlistmaker.data.dto.BaseResponse
import ussr.playlistmaker.data.dto.ItunesSearchRequest
import java.time.Instant

class RetrofitNetworkClient : NetworkClient {
    private val itunesBaseUri = "https://itunes.apple.com/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(
            Instant::class.java,
            JsonDeserializer { json, _, _ -> Instant.parse(json.asString) })
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUri)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val itunesService = retrofit.create(ItunesSearchApiService::class.java)

    override fun doRequest(dto: Any): BaseResponse {
        if (dto is ItunesSearchRequest) {
            val resp = itunesService.search(dto.request).execute()
            val body = resp.body() ?: BaseResponse()
            return body.apply { resultCode = resp.code() }
        } else {
            return BaseResponse().apply { resultCode = 400 }
        }
    }
}