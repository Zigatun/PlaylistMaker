package ussr.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = MediaPagesAdapter(requireActivity().supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) {tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favorites_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}