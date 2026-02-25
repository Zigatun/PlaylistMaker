package ussr.playlistmaker.domain.api

interface SettingsInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun switchTheme(dark: Boolean)

    fun shareApp()
    fun contactSupport()
    fun openUserAgreement()
}