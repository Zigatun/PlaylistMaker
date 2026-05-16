package ussr.playlistmaker.playlist.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ussr.playlistmaker.playlist.ui.data.CreatePlaylistState

class PlaylistCreatorViewModel: ViewModel() {
    private val _state = MutableLiveData(CreatePlaylistState())
    val state: LiveData<CreatePlaylistState> = _state

    fun onAlbumCoverPicked(uri: Uri?) {
        updateState(albumUri = uri)
    }

    fun onTitleChanged(title: String) {
        updateState(title = title)
    }

    fun onDescriptionChanged(description: String) {
        updateState(description = description)
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