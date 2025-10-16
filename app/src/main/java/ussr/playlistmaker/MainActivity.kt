package ussr.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.go_search_button)
        // способ 1: анонимный класс
        val settingsClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Поиск недоступен. Попробуйте позже.", Toast.LENGTH_SHORT).show()
            }
        }
        searchButton.setOnClickListener(settingsClickListener)
        // способ 2: лямбда-выражение
        val mediaButton = findViewById<Button>(R.id.show_media_button)
        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Медиатека недоступена. Попробуйте позже.", Toast.LENGTH_SHORT).show()
        }
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}