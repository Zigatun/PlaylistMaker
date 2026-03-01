package ussr.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ussr.playlistmaker.search.data.NetworkClient
import ussr.playlistmaker.search.data.dto.BaseResponse
import ussr.playlistmaker.search.data.dto.ItunesSearchRequest
import java.time.Instant

class RetrofitNetworkClient(private val context: Context) : NetworkClient {
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

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    override fun doRequest(dto: Any): BaseResponse {
        if (!isConnected()) {
            return BaseResponse().apply { resultCode = -1 }
        }
        if (dto !is ItunesSearchRequest) {
            return BaseResponse().apply { resultCode = 400 }
        }
        val resp = itunesService.search(dto.request).execute()
        val body = resp.body()

        return if (body != null) {
            body.apply { resultCode = resp.code() }
        } else {
            BaseResponse().apply { resultCode = resp.code() }
        }
    }
}