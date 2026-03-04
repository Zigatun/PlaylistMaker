package ussr.playlistmaker.di

import android.media.MediaPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.sharing.ExternalNavigator
import ussr.playlistmaker.sharing.impl.ExternalNavigatorImpl

val appModule = module {

    single {
        androidContext() as PlaylistMakerApp
    }

    factory {
        MediaPlayer()
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

}