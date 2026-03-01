package ussr.playlistmaker.main

import android.content.SharedPreferences
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.search.data.SearchHistoryRepositoryImpl
import ussr.playlistmaker.search.data.TracksRepositoryImpl
import ussr.playlistmaker.search.data.network.RetrofitNetworkClient
import ussr.playlistmaker.search.data.storage.SharedPreferencesStorage
import ussr.playlistmaker.search.api.SearchHistoryInteractor
import ussr.playlistmaker.search.api.SearchHistoryRepository
import ussr.playlistmaker.search.api.TracksInteractor
import ussr.playlistmaker.search.api.TracksRepository
import ussr.playlistmaker.search.impl.SearchHistoryInteractorImpl
import ussr.playlistmaker.search.impl.TracksInteractorImpl
import ussr.playlistmaker.settings.domain.SettingsInteractorImpl
import ussr.playlistmaker.settings.domain.api.SettingsInteractor
import ussr.playlistmaker.sharing.ExternalNavigator
import ussr.playlistmaker.sharing.domain.SharingInteractorImpl
import ussr.playlistmaker.sharing.domain.api.SharingInteractor
import ussr.playlistmaker.sharing.impl.ExternalNavigatorImpl
import ussr.playlistmaker.sharing.models.EmailData

object Creator {
    private fun getTracksRepository(app: PlaylistMakerApp): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(app))
    }

    fun provideTracksInteractor(app: PlaylistMakerApp): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(app))
    }

    private fun getSearchHistoryRepository(prefs: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(SharedPreferencesStorage(prefs))
    }

    fun provideSearchHistoryInteractor(app: PlaylistMakerApp): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(app.sharedPreferences))
    }

    fun provideSettingsInteractor(app: PlaylistMakerApp): SettingsInteractor {
        return SettingsInteractorImpl(app)
    }

    fun provideSharingInteractor(
        app: PlaylistMakerApp,
        shareAppLink: String,
        termsLink: String,
        emailData: EmailData
    ): SharingInteractor {
        return SharingInteractorImpl(
            provideExternalNavigator(app),
            shareAppLink = shareAppLink,
            termsLink = termsLink,
            emailData = emailData
        )
    }

    fun provideExternalNavigator(app: PlaylistMakerApp): ExternalNavigator {
        return ExternalNavigatorImpl(app)
    }
}