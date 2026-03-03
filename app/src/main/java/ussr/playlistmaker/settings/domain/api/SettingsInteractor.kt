package ussr.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun switchTheme(dark: Boolean)
}