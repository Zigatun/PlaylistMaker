package ussr.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Runnable
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ActivityPlayerBinding
import ussr.playlistmaker.main.Creator
import ussr.playlistmaker.player.ui.viewmodel.PlayerActivityViewModel
import ussr.playlistmaker.search.models.Track
import ussr.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel



    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayerBinding.inflate(layoutInflater)

        val track = intent.getParcelableExtra("track", Track::class.java)

        viewModel = ViewModelProvider(
            this, PlayerActivityViewModel.getFactory(track!!)
        ).get(PlayerActivityViewModel::class.java)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mainToolbar.setNavigationOnClickListener {
            finish()
        }
        viewModel.observableTrackState.observe(this){ state ->
            binding.trackPlayPosition.text = state.positionText
            binding.trackName.text = state.track.trackName
            binding.trackAuthor.text = state.track.artistName
            binding.duration.text = state.track.trackTime
            binding.albumGroup.isVisible = state.track.collectionName != null
            binding.album.text = state.track.collectionName
            binding.yearGroup.isVisible = state.track.yearOfRelease != null
            binding.year.text = state.track.yearOfRelease
            binding.genre.text= state.track.genreName
            binding.country.text = state.track.country

            val metrics: DisplayMetrics = this.resources.displayMetrics
            val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics)
            Glide.with(this)
                .load(state.track.coverArtworkUrl)
                .placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusPx.toInt())))
                .into(binding.albumImage)
        }
        viewModel.observableTrackState.observe(this) { state ->
            binding.trackPlayPosition.text = state.positionText
            binding.playButton.setImageResource(
                if (state.isPlaying) R.drawable.pause_icon
                else R.drawable.play_icon
            )
        }
        binding.playButton.setOnClickListener {
            viewModel.onPlayClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPausePlayer()
    }

    companion object {
        private const val CORNER_RADIUS = 8f
    }
}