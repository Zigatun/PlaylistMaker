package ussr.playlistmaker.ui.player

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Runnable
import ussr.playlistmaker.R
import ussr.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var mediaPlayer = MediaPlayer()
    private lateinit var playButton: ImageButton
    private lateinit var trackPositionTextView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val playbackTimerRunnable = object: Runnable {
        override fun run() {
            if (playerState == PLAYER_STATE_PLAYING){
                trackPositionTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, PLAYER_UPDATE_FREQ)
            }
        }
    }
    private var playerState = PLAYER_STATE_DEFAULT

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        playButton = findViewById(R.id.playButton)
        trackPositionTextView = findViewById(R.id.track_play_position)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Toolbar>(R.id.main_toolbar).setNavigationOnClickListener {
            finish()
        }
        playButton.setOnClickListener {
            when(playerState) {
                PLAYER_STATE_PLAYING -> {
                    pausePlayer()
                }
                PLAYER_STATE_PREPARED, PLAYER_STATE_PAUSED -> {
                    startPlayer()
                }
            }
        }

        val track = intent.getParcelableExtra("track", Track::class.java)
        track?.let{
            preparePlayer(track.previewUrl)
            trackPositionTextView.text = "00:00"
            findViewById<TextView>(R.id.track_name).text = it.trackName
            findViewById<TextView>(R.id.track_author).text = it.artistName
            findViewById<TextView>(R.id.duration).text = it.trackTime

            val albumGroup = findViewById<Group>(R.id.album_group)
            if(it.collectionName == null)
                albumGroup.isVisible = false
            else
                findViewById<TextView>(R.id.album).text = it.collectionName

            val yearGroup = findViewById<Group>(R.id.year_group)
            if(it.yearOfRelease == null)
                yearGroup.isVisible = false
            else
                findViewById<TextView>(R.id.year).text = it.yearOfRelease

            findViewById<TextView>(R.id.genre).text = it.genreName
            findViewById<TextView>(R.id.country).text = it.country
            val albumImage = findViewById<ImageView>(R.id.album_image)
            val radius = 8f
            val metrics: DisplayMetrics = this.resources.displayMetrics
            val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics)
            Glide.with(this)
                .load(it.coverArtworkUrl)
                .placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusPx.toInt())))
                .into(albumImage)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(playbackTimerRunnable)
    }
    private fun runPlaybackTimer(){
        handler.postDelayed(playbackTimerRunnable, PLAYER_UPDATE_FREQ)
    }
    @SuppressLint("SetTextI18n")
    private fun preparePlayer(url:String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        playButton.isEnabled = true
        mediaPlayer.setOnPreparedListener {
            trackPositionTextView.text = "00:00"
            playButton.setImageResource(R.drawable.play_icon)
            playerState = PLAYER_STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            trackPositionTextView.text = "00:00"
            playButton.setImageResource(R.drawable.play_icon)
            playerState = PLAYER_STATE_PREPARED
        }
    }
    private fun startPlayer() {
        playButton.setImageResource(R.drawable.pause_icon)
        mediaPlayer.start()
        playerState = PLAYER_STATE_PLAYING
        runPlaybackTimer()
    }

    private fun pausePlayer() {
        playButton.setImageResource(R.drawable.play_icon)
        mediaPlayer.pause()
        playerState = PLAYER_STATE_PAUSED
        handler.removeCallbacks(playbackTimerRunnable)
    }
    companion object {
        private const val PLAYER_STATE_DEFAULT = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
        private const val PLAYER_UPDATE_FREQ = 300L
    }
}