package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentFavoritesBinding
import ussr.playlistmaker.media.ui.viewmodel.FavoritesFragmentViewModel
import ussr.playlistmaker.player.ui.PlayerFragment
import ussr.playlistmaker.search.models.TracksState
import ussr.playlistmaker.search.ui.ItunesTrackAdapter

class FavoritesFragment: Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesFragmentViewModel by viewModel()

    private lateinit var trackAdapter: ItunesTrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = ItunesTrackAdapter({track -> viewModel.onTrackClicked(track)}, {})
        binding.tracksRecyclerView.adapter = trackAdapter

        viewModel.observableTrackNavigationEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { track ->
                findNavController().navigate(R.id.action_mediaFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))
            }
        }

        viewModel.observableTrackViewState().observe(viewLifecycleOwner){data ->
            when(data) {
                is TracksState.Content -> {
                    binding.errorPlaceholder.isVisible = false
                    binding.favoritesProgressBar.isVisible = false
                    binding.tracksRecyclerView.isVisible = true
                    trackAdapter.setList(data.tracks)
                }
                is TracksState.Empty -> {
                    binding.errorPlaceholder.isVisible = true
                    binding.tracksRecyclerView.isVisible = false
                    binding.favoritesProgressBar.isVisible = false
                }
                is TracksState.Error -> {
                    binding.errorPlaceholder.isVisible = true
                    binding.tracksRecyclerView.isVisible = false
                    binding.favoritesProgressBar.isVisible = false
                }
                TracksState.Loading -> {
                    binding.errorPlaceholder.isVisible = false
                    binding.tracksRecyclerView.isVisible = false
                    binding.favoritesProgressBar.isVisible = true
                }
            }
        }
        viewModel.onViewPrepared()
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}