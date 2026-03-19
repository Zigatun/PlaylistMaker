package ussr.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.databinding.ActivitySettingsBinding
import ussr.playlistmaker.settings.ui.viewmodel.SettingsActivityViewModel

class SettingsFragment : AppCompatActivity()  {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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