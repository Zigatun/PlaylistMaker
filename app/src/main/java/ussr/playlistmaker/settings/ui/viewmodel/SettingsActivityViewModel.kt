package ussr.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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

    companion object {
        fun getFactory(settingsInteractor: SettingsInteractor, sharingInteractor: SharingInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsActivityViewModel(settingsInteractor, sharingInteractor)
            }
        }
    }
}