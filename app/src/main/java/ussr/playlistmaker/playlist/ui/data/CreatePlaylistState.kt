package ussr.playlistmaker.playlist.ui.data

import android.net.Uri

data class CreatePlaylistState(val albumPhotoUri: Uri? = null, val title: String = "", val description: String = "", val allowToSave: Boolean = false)