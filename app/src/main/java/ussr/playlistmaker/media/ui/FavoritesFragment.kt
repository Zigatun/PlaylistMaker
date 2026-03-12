package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.databinding.FragmentFavoritesBinding
import ussr.playlistmaker.media.ui.viewmodel.FavoritesFragmentViewModel

class FavoritesFragment: Fragment() {

    private val viewModel: FavoritesFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFavoritesBinding.inflate(inflater, container, false).root
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}