package ussr.playlistmaker.playlist.domain

import android.net.Uri

interface PlaylistCoverRepository {
    fun saveToInternalStorage(uri: Uri): String
}