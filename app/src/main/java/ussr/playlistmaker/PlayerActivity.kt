package ussr.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ussr.playlistmaker.models.ItunesTrack

class PlayerActivity : AppCompatActivity() {
    private var mediaPlayer = MediaPlayer()
    private lateinit var playButton: ImageButton
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        playButton = findViewById(R.id.playButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<android.widget.Toolbar>(R.id.main_toolbar).setNavigationOnClickListener {
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
        val track = intent.getParcelableExtra("track", ItunesTrack::class.java)
        track?.let{
            preparePlayer(track.previewUrl)
            findViewById<TextView>(R.id.track_play_position).text = "0:00"
            findViewById<TextView>(R.id.track_name).text = it.trackName
            findViewById<TextView>(R.id.track_author).text = it.artistName
            findViewById<TextView>(R.id.duration).text = it.getHumanizedTime()

            val albumGroup = findViewById<androidx.constraintlayout.widget.Group>(R.id.album_group)
            if(it.collectionName == null)
                albumGroup.isVisible = false
            else
                findViewById<TextView>(R.id.album).text = it.collectionName

            val yearGroup = findViewById<androidx.constraintlayout.widget.Group>(R.id.year_group)
            if(it.releaseDate == null)
                yearGroup.isVisible = false
            else
                findViewById<TextView>(R.id.year).text = it.getYearOfRelease()

            findViewById<TextView>(R.id.genre).text = it.primaryGenreName
            findViewById<TextView>(R.id.country).text = it.country
            val albumImage = findViewById<ImageView>(R.id.album_image)
            val radius = 8f
            val metrics: DisplayMetrics = this.resources.displayMetrics
            val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics)
            Glide.with(this)
                .load(it.getCoverArtwork())
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
    }

    private fun preparePlayer(url:String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        playButton.isEnabled = true
        mediaPlayer.setOnPreparedListener {
            playButton.setImageResource(R.drawable.play_icon)
            playerState = PLAYER_STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.pause_icon)
            playerState = PLAYER_STATE_PREPARED
        }
    }
    private fun startPlayer() {
        playButton.setImageResource(R.drawable.pause_icon)
        mediaPlayer.start()
        playerState = PLAYER_STATE_PLAYING
    }

    private fun pausePlayer() {
        playButton.setImageResource(R.drawable.play_icon)
        mediaPlayer.pause()
        playerState = PLAYER_STATE_PAUSED
    }
    companion object {
        private const val PLAYER_STATE_DEFAULT = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
    }

    private var playerState = PLAYER_STATE_DEFAULT
}