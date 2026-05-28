package ussr.playlistmaker.di

import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ussr.playlistmaker.media.data.FavoritesRepositoryImpl
import ussr.playlistmaker.media.domain.FavoritesRepository
import ussr.playlistmaker.playlist.data.PlaylistContentRepositoryImpl
import ussr.playlistmaker.playlist.data.PlaylistCoverRepositoryImpl
import ussr.playlistmaker.playlist.data.PlaylistRepositoryImpl
import ussr.playlistmaker.playlist.domain.PlaylistContentRepository
import ussr.playlistmaker.playlist.domain.PlaylistCoverRepository
import ussr.playlistmaker.playlist.domain.PlaylistRepository
import ussr.playlistmaker.search.api.SearchHistoryRepository
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.data.SearchHistoryRepositoryImpl
import ussr.playlistmaker.search.data.TracksRepositoryImpl

val repositoryModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get<Gson>(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }

    single<PlaylistContentRepository> {
        PlaylistContentRepositoryImpl(get())
    }

    single<PlaylistCoverRepository> {
        PlaylistCoverRepositoryImpl(androidContext())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

}