package ussr.playlistmaker.settings.domain

import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsInteractorImpl(
    private val app: PlaylistMakerApp
) : SettingsInteractor {

    override fun isDarkThemeEnabled(): Boolean {
        return app.isDarkTheme
    }

    override fun switchTheme(dark: Boolean) {
        app.switchTheme(dark)
    }
}