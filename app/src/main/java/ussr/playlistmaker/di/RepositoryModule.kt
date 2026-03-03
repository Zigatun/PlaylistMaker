package ussr.playlistmaker.di

import com.google.gson.Gson
import org.koin.dsl.module
import ussr.playlistmaker.search.api.SearchHistoryRepository
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.data.SearchHistoryRepositoryImpl
import ussr.playlistmaker.search.data.TracksRepositoryImpl

val repositoryModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get<Gson>(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

}