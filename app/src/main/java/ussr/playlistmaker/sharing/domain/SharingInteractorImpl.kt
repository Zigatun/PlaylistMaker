package ussr.playlistmaker.sharing.domain

import ussr.playlistmaker.sharing.ExternalNavigator
import ussr.playlistmaker.sharing.domain.api.SharingInteractor
import ussr.playlistmaker.sharing.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val shareAppLink: String,
    private val termsLink: String,
    private val emailData : EmailData) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun contactSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun openUserAgreement() {
        externalNavigator.openLink(getTermsLink())
    }

    private fun getShareAppLink():String = shareAppLink

    private fun getSupportEmailData(): EmailData = emailData

    private fun getTermsLink(): String = termsLink
}