package ussr.playlistmaker.search.data

import ussr.playlistmaker.search.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}