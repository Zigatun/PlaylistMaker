package ussr.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ussr.playlistmaker.Creator
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ActivitySettingsBinding
import ussr.playlistmaker.settings.ui.viewmodel.SettingsActivityViewModel
import ussr.playlistmaker.sharing.models.EmailData

class SettingsActivity : AppCompatActivity()  {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val app = applicationContext as PlaylistMakerApp

        viewModel = ViewModelProvider(
            this, SettingsActivityViewModel.getFactory(
                Creator.provideSettingsInteractor(applicationContext as PlaylistMakerApp),
                Creator.provideSharingInteractor(
                    applicationContext as PlaylistMakerApp,
                    shareAppLink = app.getString(R.string.share_app),
                    termsLink = app.getString(R.string.user_agreement_link),
                    emailData = EmailData(app.getString(R.string.email_support_address),
                        app.getString(R.string.email_support_title),
                        app.getString(R.string.email_support_body))
                )
            )
        ).get(SettingsActivityViewModel::class.java)

        binding.backButton.setOnClickListener {
            finish()
        }

        viewModel.observableIsDarkThemeEnabled().observe(this){
            binding.themeSwitcher.isChecked = it
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, enabled ->
                viewModel.onThemeSwitchChanged(enabled)
        }

        binding.shareApp.setOnClickListener {
            viewModel.onShareClicked()
        }

        binding.contactWithSupport.setOnClickListener {
            viewModel.onContactSupportClicked()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.onShowUserAgreementClicked()
        }
    }
}