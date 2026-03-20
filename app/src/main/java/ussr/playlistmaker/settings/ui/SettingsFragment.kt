package ussr.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.databinding.FragmentSettingsBinding
import ussr.playlistmaker.settings.ui.viewmodel.SettingsActivityViewModel

class SettingsFragment : Fragment()  {
    private var _binding: FragmentSettingsBinding? = null
    private val viewModel: SettingsActivityViewModel by viewModel()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}