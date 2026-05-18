package ussr.playlistmaker.playlist.data

import android.content.Context
import android.net.Uri
import ussr.playlistmaker.playlist.domain.PlaylistCoverRepository
import java.io.File
import java.io.FileOutputStream

class PlaylistCoverRepositoryImpl(private val context: Context): PlaylistCoverRepository {
    override fun saveToInternalStorage(uri: Uri): String {
        val fileName = "playlist_cover_${System.currentTimeMillis()}.jpg"

        val coversDir = File(context.filesDir, "playlist_covers")
        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }

        val outputFile = File(coversDir, fileName)

        context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Cannot open input stream for uri: $uri" }

            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        return outputFile.absolutePath
    }
}