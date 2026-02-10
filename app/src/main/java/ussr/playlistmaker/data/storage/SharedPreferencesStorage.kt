package ussr.playlistmaker.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import ussr.playlistmaker.data.StorageService

class SharedPreferencesStorage(private val prefs: SharedPreferences): StorageService {
    override fun get(key: String): String {
        return prefs.getString(key, "")!!
    }

    override fun put(key: String, payload: String) {
        prefs.edit { putString(key, payload) }
    }
}