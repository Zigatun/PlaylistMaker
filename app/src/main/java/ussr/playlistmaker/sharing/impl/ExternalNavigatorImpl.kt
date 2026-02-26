package ussr.playlistmaker.sharing.impl

import android.content.Intent
import androidx.core.net.toUri
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.R
import ussr.playlistmaker.sharing.ExternalNavigator
import ussr.playlistmaker.sharing.models.EmailData

class ExternalNavigatorImpl(private val app: PlaylistMakerApp): ExternalNavigator {
    val context: PlaylistMakerApp = app

    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }

    override fun openLink(link: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            link.toUri()
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.address))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.body)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }
}