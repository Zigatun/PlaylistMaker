package ussr.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ussr.playlistmaker.search.data.NetworkClient
import ussr.playlistmaker.search.data.dto.BaseResponse
import ussr.playlistmaker.search.data.dto.ItunesSearchRequest

class RetrofitNetworkClient(private val itunesService: ItunesSearchApiService, private val context: Context) : NetworkClient {

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

    override suspend fun doRequest(dto: Any): BaseResponse {
        if (!isConnected()) {
            return BaseResponse().apply { resultCode = -1 }
        }
        if (dto !is ItunesSearchRequest) {
            return BaseResponse().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO){
            try {
                val response = itunesService.search(dto.request)
                response.apply { resultCode = 200 }
            } catch (_: Throwable){
                BaseResponse().apply { resultCode = 200 }
            }
        }
    }
}