package ussr.playlistmaker.playlist.ui.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.domain.PlaylistCoverInteractor
import ussr.playlistmaker.playlist.domain.PlaylistInteractor
import ussr.playlistmaker.playlist.ui.data.CreatePlaylistEvent
import ussr.playlistmaker.playlist.ui.data.CreatePlaylistState
import java.io.File

class PlaylistCreatorViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val playlist: PlaylistModel?
) : ViewModel() {
    private val _state = MutableLiveData(CreatePlaylistState())
    val state: LiveData<CreatePlaylistState> = _state

    private val _event = MutableLiveData<CreatePlaylistEvent>()
    val event: LiveData<CreatePlaylistEvent> = _event

    fun onAlbumCoverPicked(uri: Uri?) {
        updateState(albumUri = uri)
    }

    fun onTitleChanged(title: String) {
        updateState(title = title)
    }

    fun onDescriptionChanged(description: String) {
        updateState(description = description)
    }

    fun canGracefullyLeave(): Boolean{
        val currentState = _state.value!!
        return currentState.title.isBlank() && currentState.description.isBlank() && currentState.albumPhotoUri == null
    }

    fun initForEdit(playlist: PlaylistModel) {
        updateState(
            albumUri = playlist.imagePath
                ?.takeIf { it.isNotBlank() }
                ?.let { File(it).toUri() },
            title = playlist.title,
            description = playlist.description
        )
    }

    fun onSavePressed() {
        val currentState = _state.value ?: return
        if (!currentState.allowToSave) return

        viewModelScope.launch {
            if(playlist == null){
                playlistInteractor.createPlaylist(
                    PlaylistModel(
                        title = currentState.title,
                        description = currentState.description,
                        imagePath = playlistCoverInteractor.saveToInternalStorage(currentState.albumPhotoUri),
                        content = mutableListOf()
                    )
                )

                _event.value = CreatePlaylistEvent.PlaylistCreated(currentState.title)

            } else {
                playlistInteractor.modifyPlaylist(playlist.apply {
                    title = currentState.title
                    description = currentState.description
                    imagePath = playlistCoverInteractor.saveToInternalStorage(currentState.albumPhotoUri)
                })
                _event.value = CreatePlaylistEvent.PlaylistModified(currentState.title)
            }
        }
    }

    private fun updateState(
        albumUri: Uri? = _state.value?.albumPhotoUri,
        title: String? = _state.value?.title,
        description: String? = _state.value?.description
    ) {
        val newState = CreatePlaylistState(
            albumPhotoUri = albumUri,
            title = title.orEmpty(),
            description = description.orEmpty()
        )

        _state.value = newState.copy(
            allowToSave = newState.title.isNotBlank()
        )
    }
}