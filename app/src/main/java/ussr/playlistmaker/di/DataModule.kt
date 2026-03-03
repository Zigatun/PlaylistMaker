package ussr.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ussr.playlistmaker.search.data.NetworkClient
import ussr.playlistmaker.search.data.StorageService
import ussr.playlistmaker.search.data.network.ItunesSearchApiService
import ussr.playlistmaker.search.data.network.RetrofitNetworkClient
import ussr.playlistmaker.search.data.storage.SharedPreferencesStorage
import java.time.Instant

const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"

val dataModule = module {

    single<ItunesSearchApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .build()
            .create(ItunesSearchApiService::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<StorageService> {
        SharedPreferencesStorage(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<Gson> {
        GsonBuilder()
            .registerTypeAdapter(
                Instant::class.java,
                JsonSerializer<Instant> { src, _, _ ->
                    JsonPrimitive(src.toString())
                }
            )
            .registerTypeAdapter(
                Instant::class.java,
                JsonDeserializer { json, _, _ -> Instant.parse(json.asString) })
            .create()
    }

}