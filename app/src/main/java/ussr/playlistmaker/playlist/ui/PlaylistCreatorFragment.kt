package ussr.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import ussr.playlistmaker.playlist.ui.viewmodel.PlaylistCreatorViewModel

class PlaylistCreatorFragment: Fragment() {
    private var _binding: FragmentPlaylistCreatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistCreatorViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner){state ->
            binding.createButton.isEnabled = state.allowToSave
        }
        binding.playlistName.doAfterTextChanged { viewModel.onTitleChanged(it.toString()) }
        binding.playlistDescription.doAfterTextChanged { viewModel.onDescriptionChanged(it.toString()) }

    }
}