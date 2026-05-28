package ussr.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentPlayerBinding
import ussr.playlistmaker.media.ui.data.PlaylistsState
import ussr.playlistmaker.player.ui.viewmodel.PlayerActivityViewModel
import ussr.playlistmaker.playlist.ui.data.PlaylistTrackAddEvent
import ussr.playlistmaker.search.models.Track
import kotlin.jvm.java

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: PlayerActivityViewModel
    lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var playlistsAdapter: PlaylistHorizontalCardAdapter

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistAddSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                syncOverlay()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = requireArguments().getParcelable(ARGS_TRACK, Track::class.java)

        playlistsAdapter = PlaylistHorizontalCardAdapter({playlist ->
            viewModel.onPlaylistSelected(playlist, track!!)
        })

        binding.playlistsRecyclerView.adapter = playlistsAdapter

        binding.mainToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel = getViewModel {
            parametersOf(requireNotNull(track))
        }

        binding.trackName.text = track?.trackName
        binding.trackAuthor.text = track?.artistName
        binding.duration.text = track?.trackTime
        binding.albumGroup.isVisible = track?.collectionName != null
        binding.album.text = track?.collectionName
        binding.yearGroup.isVisible = track?.yearOfRelease != null
        binding.year.text = track?.yearOfRelease
        binding.genre.text= track?.genreName
        binding.country.text = track?.country
        binding.addToFavorites.setImageResource(if (track?.isFavorite == true) R.drawable.remove_from_favorites_icon else R.drawable.add_to_favorites_icon)
        val metrics: DisplayMetrics = this.resources.displayMetrics
        val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics)
        Glide.with(this)
            .load(track?.coverArtworkUrl)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusPx.toInt())))
            .into(binding.albumImage)

        viewModel.observableTrackState.observe(viewLifecycleOwner){ state ->
            binding.trackPlayPosition.text = state.progress
            binding.addToFavorites.setImageResource(if (state.isInFavorites) R.drawable.remove_from_favorites_icon else R.drawable.add_to_favorites_icon)
            binding.playButton.setImageResource(
                if (state.isInPlayMode) R.drawable.pause_icon
                else R.drawable.play_icon
            )
        }

        viewModel.observablePlaylistViewState().observe(viewLifecycleOwner){ data ->
            when(data) {
                is PlaylistsState.Content -> {
                    binding.progressBar.isVisible = false
                    binding.playlistsRecyclerView.isVisible = true
                    playlistsAdapter.setList(data.playlists)
                }
                is PlaylistsState.Empty -> {
                    binding.progressBar.isVisible = false
                    binding.playlistsRecyclerView.isVisible = false
                }
                PlaylistsState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.playlistsRecyclerView.isVisible = false
                }
            }
        }
        viewModel.trackAddEvent.observe(viewLifecycleOwner) { event ->

            when (event) {
                is PlaylistTrackAddEvent.TrackAdded -> {
                    Toast.makeText(
                        requireContext(),
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is PlaylistTrackAddEvent.TrackAlreadyExists -> {
                    Toast.makeText(
                        requireContext(),
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
        }
//        viewModel.observableIsInFavorites.observe(viewLifecycleOwner){ isInFavorites ->
//            binding.addToFavorites.setImageResource(if (isInFavorites == true) R.drawable.remove_from_favorites_icon else R.drawable.add_to_favorites_icon)
//        }

        binding.addToFavorites.setOnClickListener {
            viewModel.onFavoritesClicked()
        }
        binding.addNewPlaylist.setOnClickListener {
            setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
            findNavController().navigate(R.id.action_playerFragment_to_playlistCreatorFragment)
        }
        binding.playButton.setOnClickListener {
            viewModel.onPlayClicked()
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.loadPlaylists()
            setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
        }

    }

    override fun onResume() {
        super.onResume()
        syncOverlay()
        if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN){
            viewModel.loadPlaylists()
        }
    }
    private fun setBottomSheetState(state: Int){
        bottomSheetBehavior.state = state
        syncOverlay()
    }
    private fun syncOverlay() {
        binding.overlay.visibility =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                View.GONE
            } else {
                View.VISIBLE
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPausePlayer()
    }

    companion object {
        private const val CORNER_RADIUS = 8f
        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle = bundleOf(ARGS_TRACK to track)
    }
}