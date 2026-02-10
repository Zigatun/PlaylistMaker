package ussr.playlistmaker

import ussr.playlistmaker.data.TracksRepositoryImpl
import ussr.playlistmaker.data.network.RetrofitNetworkClient
import ussr.playlistmaker.domain.api.TracksInteractor
import ussr.playlistmaker.domain.api.TracksRepository
import ussr.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}