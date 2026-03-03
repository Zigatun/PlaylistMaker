package ussr.playlistmaker.settings.domain

import android.content.SharedPreferences
import ussr.playlistmaker.settings.domain.api.SettingsInteractor
const val OPT_IS_DARK_THEME = "OPT_IS_DARK_THEME"

class SettingsInteractorImpl(
    private val prefs: SharedPreferences
) : SettingsInteractor {

    override fun isDarkThemeEnabled(): Boolean {
        return prefs.getBoolean(OPT_IS_DARK_THEME, false)
    }

    override fun switchTheme(dark: Boolean) {
        prefs.edit().putBoolean(OPT_IS_DARK_THEME, dark).apply()
    }
}