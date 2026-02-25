package ussr.playlistmaker

import android.content.SharedPreferences

import ussr.playlistmaker.data.SearchHistoryRepositoryImpl
import ussr.playlistmaker.data.TracksRepositoryImpl
import ussr.playlistmaker.data.network.RetrofitNetworkClient
import ussr.playlistmaker.data.storage.SharedPreferencesStorage
import ussr.playlistmaker.domain.api.SearchHistoryInteractor
import ussr.playlistmaker.domain.api.SearchHistoryRepository
import ussr.playlistmaker.domain.api.SettingsInteractor
import ussr.playlistmaker.domain.api.TracksInteractor
import ussr.playlistmaker.domain.api.TracksRepository
import ussr.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import ussr.playlistmaker.domain.impl.SettingsInteractorImpl
import ussr.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(prefs: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(SharedPreferencesStorage(prefs))
    }

    fun provideSearchHistoryInteractor(app: PlaylistMakerApp): SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(app.sharedPreferences))
    }

    fun provideSettingsInteractor(app: PlaylistMakerApp): SettingsInteractor {
        return SettingsInteractorImpl(app)
    }
}