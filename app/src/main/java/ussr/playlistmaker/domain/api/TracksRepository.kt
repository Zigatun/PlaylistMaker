package ussr.playlistmaker.domain.api

import ussr.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(searchPattern: String): List<Track>
}