package ussr.playlistmaker.playlist.ui.viewmodel

import android.net.Uri
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

class PlaylistCreatorViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor
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

    fun onSavePressed() {
        val currentState = _state.value ?: return
        if (!currentState.allowToSave) return

        viewModelScope.launch {
            if (currentState.albumPhotoUri != null) {

            }

            playlistInteractor.CreatePlaylist(
                PlaylistModel(
                    title = currentState.title,
                    description = currentState.description,
                    imagePath = playlistCoverInteractor.saveToInternalStorage(currentState.albumPhotoUri),
                    content = emptyList()
                )
            )

            _event.value = CreatePlaylistEvent.PlaylistCreated(currentState.title)
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