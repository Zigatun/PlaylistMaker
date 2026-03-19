package ussr.playlistmaker.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ActivityMediaBinding

class MediaFragment : AppCompatActivity(R.layout.fragment_media) {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainToolbar.setNavigationOnClickListener { finish() }

        binding.pager.adapter = MediaPagesAdapter(supportFragmentManager, lifecycle)
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