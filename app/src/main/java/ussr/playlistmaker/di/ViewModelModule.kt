package ussr.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ussr.playlistmaker.media.ui.viewmodel.FavoritesFragmentViewModel
import ussr.playlistmaker.media.ui.viewmodel.UserPlaylistsFragmentViewModel
import ussr.playlistmaker.player.ui.viewmodel.PlayerActivityViewModel
import ussr.playlistmaker.playlist.data.models.PlaylistModel
import ussr.playlistmaker.playlist.ui.viewmodel.PlaylistCreatorViewModel
import ussr.playlistmaker.playlist.ui.viewmodel.PlaylistEditorViewModel
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import ussr.playlistmaker.settings.ui.viewmodel.SettingsActivityViewModel

val viewModelModule = module {

    viewModel {
        SearchActivityViewModel(get(), get())
    }

    viewModel {
        FavoritesFragmentViewModel(get())
    }

    viewModel {
        UserPlaylistsFragmentViewModel(get())
    }

    viewModel { (playlist: PlaylistModel?) ->
        PlaylistCreatorViewModel(get(), get(), playlist)
    }

    viewModel { (playlist: PlaylistModel) ->
        PlaylistEditorViewModel(get(), get(), playlist)
    }

    viewModel {
        SettingsActivityViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        PlayerActivityViewModel(get(), get(), get(), track)
    }

}