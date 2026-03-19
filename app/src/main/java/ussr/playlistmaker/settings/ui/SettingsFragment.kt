package ussr.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.databinding.FragmentMediaBinding
import ussr.playlistmaker.databinding.FragmentSettingsBinding
import ussr.playlistmaker.settings.ui.viewmodel.SettingsActivityViewModel

class SettingsFragment : Fragment()  {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsActivityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        viewModel.observableIsDarkThemeEnabled().observe(viewLifecycleOwner){
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