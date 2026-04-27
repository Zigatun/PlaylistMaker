package ussr.playlistmaker.search.data

import ussr.playlistmaker.search.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}