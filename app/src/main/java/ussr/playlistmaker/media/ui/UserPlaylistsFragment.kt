package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ussr.playlistmaker.databinding.FragmentUserPlaylistsBinding

class UserPlaylistsFragment: Fragment() {
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