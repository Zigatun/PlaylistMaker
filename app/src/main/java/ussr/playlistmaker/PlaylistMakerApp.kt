package ussr.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
const val OPT_IS_DARK_THEME = "opt_is_dark_theme"

class PlaylistMakerApp : Application() {

    var isDarkTheme = false
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        isDarkTheme = sharedPreferences.getBoolean(OPT_IS_DARK_THEME, isDarkTheme)
        switchTheme(isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}