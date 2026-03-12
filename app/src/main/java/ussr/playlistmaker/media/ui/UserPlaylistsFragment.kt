package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.databinding.FragmentUserPlaylistsBinding
import ussr.playlistmaker.media.ui.viewmodel.UserPlaylistsFragmentViewModel

class UserPlaylistsFragment: Fragment() {

    private val viewModel: UserPlaylistsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserPlaylistsBinding.inflate(inflater, container, false).root
    }

    companion object {
        fun newInstance() = UserPlaylistsFragment()
    }
}