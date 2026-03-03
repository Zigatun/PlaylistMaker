package ussr.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ussr.playlistmaker.player.ui.viewmodel.PlayerActivityViewModel
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import ussr.playlistmaker.settings.ui.viewmodel.SettingsActivityViewModel

val viewModelModule = module {

    viewModel {
        SearchActivityViewModel(get(), get())
    }

    viewModel {
        SettingsActivityViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        PlayerActivityViewModel(track)
    }

}