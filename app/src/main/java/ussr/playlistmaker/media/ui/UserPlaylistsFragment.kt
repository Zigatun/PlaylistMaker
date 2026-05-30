package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentUserPlaylistsBinding
import ussr.playlistmaker.media.ui.data.PlaylistsState
import ussr.playlistmaker.media.ui.viewmodel.UserPlaylistsFragmentViewModel
import ussr.playlistmaker.playlist.ui.PlaylistEditorFragment

class UserPlaylistsFragment: Fragment() {
    private var _binding: FragmentUserPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserPlaylistsFragmentViewModel by viewModel()

    private lateinit var playlistsAdapter: PlaylistAdapter

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
        playlistsAdapter = PlaylistAdapter( { playlist ->
            viewModel.onPlaylistClicked(playlist)
        })
        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.playlistsRecyclerView.adapter = playlistsAdapter

        binding.createNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistCreatorFragment)
        }

        viewModel.observablePlaylistNavigationEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { playlist ->
                findNavController().navigate(R.id.action_mediaFragment_to_playlistEditorFragment,
                    PlaylistEditorFragment.createArgs(playlist))
            }
        }

        viewModel.observablePlaylistViewState().observe(viewLifecycleOwner){ data ->
            when(data) {
                is PlaylistsState.Content -> {
                    binding.progressBar.isVisible = false
                    binding.createNewPlaylist.isVisible = true
                    binding.noItemsPlaceholder.isVisible = false
                    binding.playlistsRecyclerView.isVisible = true
                    playlistsAdapter.setList(data.playlists)
                }
                is PlaylistsState.Empty -> {
                    binding.progressBar.isVisible = false
                    binding.createNewPlaylist.isVisible = true
                    binding.noItemsPlaceholder.isVisible = true
                    binding.playlistsRecyclerView.isVisible = false
                }
                PlaylistsState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.createNewPlaylist.isVisible = false
                    binding.noItemsPlaceholder.isVisible = false
                    binding.playlistsRecyclerView.isVisible = false
                }
            }
        }
        viewModel.onViewPrepared()
    }
    companion object {
        fun newInstance() = UserPlaylistsFragment()
    }
}