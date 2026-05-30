package ussr.playlistmaker.playlist.ui

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentPlaylistEditorBinding
import ussr.playlistmaker.player.ui.PlayerFragment
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.ui.data.SharePlaylistEvent
import ussr.playlistmaker.playlist.ui.viewmodel.PlaylistEditorViewModel
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.ui.ItunesTrackAdapter

class PlaylistEditorFragment: Fragment()  {
    private var _binding: FragmentPlaylistEditorBinding? = null
    private lateinit var viewModel: PlaylistEditorViewModel

    private lateinit var tracksAdapter: ItunesTrackAdapter

    lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistEditorBinding.inflate(inflater, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.moreActionsSheet)
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

    fun durationToSeconds(value: String): Int {
        val (minutes, seconds) = value.split(":").map(String::toInt)
        return minutes + if (seconds > 0) 1 else 0
    }
    fun formatTrackCount(content: List<Track>): String = when {
        content.isEmpty() -> "Плейлист пуст"
        content.size % 100 in 11..14 -> "${content.size} треков"
        content.size % 10 == 1 -> "${content.size} трек"
        content.size % 10 in 2..4 -> "${content.size} трека"
        else ->  "${content.size} треков"
    }
    fun sharePlaylist(playlist:PlaylistModel, content: List<Track>){
        var playlistContent: String = "${playlist.title}\n"
        playlistContent += "${playlist.description}\n"
        playlistContent += "${formatTrackCount(content)}\n\n"
        var playlistIterator = 1
        content.forEach { t ->
            playlistContent += "${playlistIterator++}. ${t.artistName} - ${t.trackName} (${t.trackTime})"
            if(playlistIterator <= content.size)
                playlistContent += "\n"
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, playlistContent)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext().startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.doOnLayout {
            val rootLocation = IntArray(2)
            val buttonsLocation = IntArray(2)

            binding.root.getLocationOnScreen(rootLocation)
            binding.actionButtons.getLocationOnScreen(buttonsLocation)

            val buttonsBottomInRoot =
                buttonsLocation[1] - rootLocation[1] + binding.actionButtons.height + binding.actionButtons.marginBottom

            val sheetHeight = binding.root.height - buttonsBottomInRoot

            BottomSheetBehavior.from(binding.playlistContentSheet).peekHeight = sheetHeight
        }
        val playlist = requireArguments().getParcelable(ARGS_PLAYLIST, PlaylistModel::class.java)

        tracksAdapter = ItunesTrackAdapter({track ->
            viewModel.onTrackClicked(track)
        }, {track ->
            viewModel.onTrackToBeRemoved(track)
        })

        binding.tracksRecyclerView.adapter = tracksAdapter

        viewModel = getViewModel {
            parametersOf(requireNotNull(playlist))
        }

        binding.mainToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.sharePlaylist.setOnClickListener {
            viewModel.onShareClicked()
        }

        binding.sharePlaylistLow.setOnClickListener {
            viewModel.onShareClicked()
        }

        binding.more.setOnClickListener {
            setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharePlaylistEvent.collect {
                    when(it) {
                        is SharePlaylistEvent.LetsDoIt -> sharePlaylist(playlist!!, it.payload)
                        is SharePlaylistEvent.NoContent -> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show() }
                }
            }
        }


        viewModel.PlaylistContentState().observe(viewLifecycleOwner,
            { data ->
                tracksAdapter.setList(data)
                val durationSum = data.sumOf { t-> durationToSeconds(t.trackTime) }
                binding.playlistLength.text = "$durationSum минут"
                binding.playlistTracksCount.text = formatTrackCount(data)
                binding.root.requestLayout()
            })

        viewModel.TrackNavigationEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { track ->
                findNavController().navigate(R.id.action_playlistEditorFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))
            }
        }

        viewModel.TrackRemovingEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { track ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Удаление трека")
                    .setMessage("Хотите удалить трек \"${track.artistName} - ${track.trackName}\"?")
                    .setPositiveButton("Да", { _,_ ->
                        viewModel.removeTrackFromPlaylist(track)
                    })
                    .setNegativeButton("Нет", null)
                    .show()
            }
        }

        binding.editInfo.setOnClickListener {
            viewModel.onPlaylistInfoToBeChanged()
        }
        viewModel.PlaylistInfoNavigationEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { playlist ->
                findNavController().navigate(R.id.action_playlistEditorFragment_to_playlistCreatorFragment,
                    PlaylistCreatorFragment.createArgs(playlist))
            }
        }

        binding.removePlaylist.setOnClickListener {
            viewModel.onPlaylistToBeRemoved()
        }
        viewModel.PlaylistRemovingEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { playlist ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Хотите удалить плейлист \"${playlist.title}?")
                    .setPositiveButton("Да", { _,_ ->
                        viewModel.removePlaylist()
                        findNavController().popBackStack()
                    })
                    .setNegativeButton("Нет", null)
                    .show()
            }
        }

        binding.playlistNameLow.text = playlist?.title
        binding.playlistTitle.text = playlist?.title

        binding.playlistDescription.text = playlist?.description
        binding.playlistDescriptionLow.text = playlist?.description

        binding.root.requestLayout()
        Glide.with(binding.root)
            .load(playlist?.imagePath)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .into(binding.playlistImage)
        Glide.with(binding.root)
                    .load(playlist?.imagePath)
                    .placeholder(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(binding.playlistImageLow)

        viewModel.onViewPrepared()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: PlaylistModel): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }
}