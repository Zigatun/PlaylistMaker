package ussr.playlistmaker.playlist.domain

import android.net.Uri

interface PlaylistCoverInteractor {
    fun saveToInternalStorage(uri: Uri?): String?
}