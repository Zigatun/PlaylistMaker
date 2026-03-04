package ussr.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ussr.playlistmaker.di.appModule
import ussr.playlistmaker.di.dataModule
import ussr.playlistmaker.di.interactorModule
import ussr.playlistmaker.di.repositoryModule
import ussr.playlistmaker.di.viewModelModule
import ussr.playlistmaker.settings.domain.api.SettingsInteractor


class PlaylistMakerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistMakerApp)
            modules(appModule, dataModule, interactorModule, repositoryModule, viewModelModule)
        }
        val settingsInteractor: SettingsInteractor =
            org.koin.java.KoinJavaComponent.get(SettingsInteractor::class.java)

        val isDarkTheme = settingsInteractor.isDarkThemeEnabled()
        switchTheme(isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}