package ussr.playlistmaker.data.dto

data class ItunesSearchResponse (val resultCount: Long, val results: List<TrackDto>) : BaseResponse()