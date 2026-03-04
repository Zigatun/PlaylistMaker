package ussr.playlistmaker.settings.ui.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ussr.playlistmaker.settings.domain.api.SettingsInteractor
import ussr.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsActivityViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor): ViewModel() {

    private val isDarkThemeEnabledLiveData = MutableLiveData<Boolean>(settingsInteractor.isDarkThemeEnabled())
    fun observableIsDarkThemeEnabled(): LiveData<Boolean> = isDarkThemeEnabledLiveData

    fun onThemeSwitchChanged(enabled: Boolean){
        settingsInteractor.switchTheme(enabled)
        isDarkThemeEnabledLiveData.value = enabled
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun onShareClicked(){
        sharingInteractor.shareApp()
    }

    fun onShowUserAgreementClicked(){
        sharingInteractor.openUserAgreement()
    }

    fun onContactSupportClicked(){
        sharingInteractor.contactSupport()
    }
}