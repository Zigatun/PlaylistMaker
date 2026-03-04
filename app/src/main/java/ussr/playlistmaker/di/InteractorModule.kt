package ussr.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ussr.playlistmaker.R
import ussr.playlistmaker.search.api.SearchHistoryInteractor
import ussr.playlistmaker.search.api.TracksInteractor
import ussr.playlistmaker.search.impl.SearchHistoryInteractorImpl
import ussr.playlistmaker.search.impl.TracksInteractorImpl
import ussr.playlistmaker.settings.domain.SettingsInteractorImpl
import ussr.playlistmaker.settings.domain.api.SettingsInteractor
import ussr.playlistmaker.sharing.domain.SharingInteractorImpl
import ussr.playlistmaker.sharing.domain.api.SharingInteractor
import ussr.playlistmaker.sharing.models.EmailData

val interactorModule = module {

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            get(),
            shareAppLink = androidContext().getString(R.string.share_app),
            termsLink = androidContext().getString(R.string.user_agreement_link),
            emailData = EmailData(
                androidContext().getString(R.string.email_support_address),
                androidContext().getString(R.string.email_support_title),
                androidContext().getString(R.string.email_support_body)
            )
        )
    }

}