package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import ussr.playlistmaker.databinding.FragmentUserPlaylistsBinding
import ussr.playlistmaker.media.ui.viewmodel.UserPlaylistsFragmentViewModel

class UserPlaylistsFragment: Fragment() {
    private var _binding: FragmentUserPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserPlaylistsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistCreatorFragment)
        }
    }
    companion object {
        fun newInstance() = UserPlaylistsFragment()
    }
}