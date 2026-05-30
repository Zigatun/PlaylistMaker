package ussr.playlistmaker.playlist.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.allGranted
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.ui.data.CreatePlaylistEvent
import ussr.playlistmaker.playlist.ui.viewmodel.PlaylistCreatorViewModel

class PlaylistCreatorFragment: Fragment() {
    private var _binding: FragmentPlaylistCreatorBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlaylistCreatorViewModel

    private var playlist: PlaylistModel? = null
    val requester = PermissionRequester.instance()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.onAlbumCoverPicked(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainToolbar.setNavigationOnClickListener {
            promptUserForUnsavedChanges()
        }
        playlist = arguments?.getParcelable(ARGS_PLAYLIST, PlaylistModel::class.java)

        viewModel = getViewModel {
            parametersOf(playlist)
        }
        viewModel.state.observe(viewLifecycleOwner){state ->
            binding.createButton.isEnabled = state.allowToSave

            val metrics: DisplayMetrics = this.resources.displayMetrics
            val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics)
            Glide.with(this)
                .load(state.albumPhotoUri)
                .placeholder(R.drawable.album_art_pick_icon)
                .transform(
                    CenterCrop(),
                    RoundedCorners(radiusPx.toInt())
                )
                .into(binding.playlistImage)
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is CreatePlaylistEvent.PlaylistCreated -> {
                    Toast.makeText(
                        requireContext(),
                        "Плейлист ${event.playlistTitle} создан",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()
                }

                is CreatePlaylistEvent.PlaylistModified -> {
                    Toast.makeText(
                        requireContext(),
                        "Плейлист ${event.playlistTitle} изменён",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()
                }
            }
        }

        binding.playlistImage.setOnClickListener {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            viewLifecycleOwner.lifecycleScope.launch {

                if (shouldShowRequestPermissionRationale(permission)) {
                    Toast.makeText(context, R.string.permission_description, Toast.LENGTH_LONG).show()
                }

                val granted = requester
                    .request(permission)
                    .allGranted()

                if (granted) {
                    pickMedia.launch("image/*")
                } else {
                    val c = requireContext()
                    MaterialAlertDialogBuilder(c)
                        .setTitle(R.string.no_permission)
                        .setMessage(R.string.no_permission_description)
                        .setPositiveButton(R.string.yes, {_, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data= Uri.fromParts("package", c.packageName, null)
                            context?.startActivity(intent)
                        })
                        .setNegativeButton(R.string.no, null)
                        .show()
                }
            }

        }

        binding.playlistName.doAfterTextChanged { viewModel.onTitleChanged(it.toString()) }
        binding.playlistDescription.doAfterTextChanged { viewModel.onDescriptionChanged(it.toString()) }

        binding.createButton.setOnClickListener { viewModel.onSavePressed() }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    promptUserForUnsavedChanges()
                }
            }
        )
        if (playlist != null) {
            binding.mainToolbar.title = "Редактировать"
            binding.createButton.text = "Сохранить"

            binding.playlistName.setText(playlist?.title)
            binding.playlistDescription.setText(playlist?.description)

            viewModel.initForEdit(playlist!!)
        }
    }

    private fun promptUserForUnsavedChanges() {

        if (!viewModel.canGracefullyLeave()) {
            if(playlist == null)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(if (playlist == null) "Завершить создание плейлиста?" else "Завершить редактирование плейлиста?")
                .setMessage("Все несохранённые данные будут потеряны")
                .setPositiveButton("Завершить") { _, _ ->
                    findNavController().popBackStack()
                }
                .setNegativeButton("Отмена", null)
                .show()

        } else {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val CORNER_RADIUS = 8f
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: PlaylistModel): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }
}