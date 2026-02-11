package ussr.playlistmaker.ui.settings

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import ussr.playlistmaker.Creator
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.R
import ussr.playlistmaker.domain.api.SettingsInteractor

class SettingsActivity : AppCompatActivity()  {
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsInteractor = Creator.provideSettingsInteractor(applicationContext as PlaylistMakerApp)

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }

        val switcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        switcher.isChecked = settingsInteractor.isDarkThemeEnabled()
        switcher.setOnCheckedChangeListener { _, enabled ->
            settingsInteractor.switchTheme(enabled)
        }

        findViewById<FrameLayout>(R.id.share_app).setOnClickListener{
            settingsInteractor.shareApp()
        }

        findViewById<FrameLayout>(R.id.contact_with_support).setOnClickListener {
            settingsInteractor.contactSupport()
        }

        findViewById<FrameLayout>(R.id.user_agreement).setOnClickListener {
            settingsInteractor.openUserAgreement()
        }
    }
}