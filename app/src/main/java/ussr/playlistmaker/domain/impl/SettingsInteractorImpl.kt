package ussr.playlistmaker.domain.impl

import android.content.Intent
import androidx.core.net.toUri
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.R
import ussr.playlistmaker.domain.api.SettingsInteractor

class SettingsInteractorImpl(
    private val app: PlaylistMakerApp
) : SettingsInteractor {

    override fun isDarkThemeEnabled(): Boolean {
        return app.isDarkTheme
    }

    override fun switchTheme(dark: Boolean) {
        app.switchTheme(dark)
    }

    override fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, app.getString(R.string.share_link))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }

    override fun contactSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(app.getString(R.string.email_support_address)))
            putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.email_support_title))
            putExtra(Intent.EXTRA_TEXT, app.getString(R.string.email_support_body))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }

    override fun openUserAgreement() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            app.getString(R.string.user_agreement_link).toUri()
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }
}
