package ussr.playlistmaker.playlist.impl

import android.net.Uri
import ussr.playlistmaker.playlist.domain.PlaylistCoverInteractor
import ussr.playlistmaker.playlist.domain.PlaylistCoverRepository

class PlaylistCoverInteractorImpl(private val repository: PlaylistCoverRepository): PlaylistCoverInteractor {
    override fun saveToInternalStorage(uri: Uri?): String? {
        if(uri == null)
            return null
        return repository.saveToInternalStorage(uri)
    }
}