package ussr.playlistmaker

import android.content.SharedPreferences

import ussr.playlistmaker.data.SearchHistoryRepositoryImpl
import ussr.playlistmaker.data.TracksRepositoryImpl
import ussr.playlistmaker.data.network.RetrofitNetworkClient
import ussr.playlistmaker.data.storage.SharedPreferencesStorage
import ussr.playlistmaker.domain.api.SearchHistoryInteractor
import ussr.playlistmaker.domain.api.SearchHistoryRepository
import ussr.playlistmaker.settings.domain.api.SettingsInteractor
import ussr.playlistmaker.domain.api.TracksInteractor
import ussr.playlistmaker.domain.api.TracksRepository
import ussr.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import ussr.playlistmaker.settings.domain.SettingsInteractorImpl
import ussr.playlistmaker.domain.impl.TracksInteractorImpl
import ussr.playlistmaker.sharing.ExternalNavigator
import ussr.playlistmaker.sharing.domain.SharingInteractorImpl
import ussr.playlistmaker.sharing.domain.api.SharingInteractor
import ussr.playlistmaker.sharing.impl.ExternalNavigatorImpl
import ussr.playlistmaker.sharing.models.EmailData

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