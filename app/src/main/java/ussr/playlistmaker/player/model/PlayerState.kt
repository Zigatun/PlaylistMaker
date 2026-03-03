package ussr.playlistmaker.player.model

import ussr.playlistmaker.search.models.Track

data class PlayerState(
    val track: Track,
    val positionText: String = "00:00",
    val isPlaying: Boolean = false
)