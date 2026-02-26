package ussr.playlistmaker.sharing

import ussr.playlistmaker.sharing.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}