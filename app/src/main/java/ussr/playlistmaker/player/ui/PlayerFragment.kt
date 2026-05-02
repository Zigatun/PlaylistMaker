package ussr.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentPlayerBinding
import ussr.playlistmaker.player.ui.viewmodel.PlayerActivityViewModel
import ussr.playlistmaker.search.models.Track
import kotlin.jvm.java

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: PlayerActivityViewModel

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainToolbar.setNavigationOnClickListener {
            val result = findNavController().popBackStack()
            Log.d("NAV", "popBackStack result = $result")
        }

        val track = requireArguments().getParcelable(ARGS_TRACK, Track::class.java)
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
            binding.playButton.setImageResource(
                if (state.isInPlayMode) R.drawable.pause_icon
                else R.drawable.play_icon
            )
        }
        binding.playButton.setOnClickListener {
            viewModel.onPlayClicked()
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