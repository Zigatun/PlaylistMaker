package ussr.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        findViewById<FrameLayout>(R.id.share_app).setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.share_link));
            startActivity(shareIntent)
        }

        findViewById<FrameLayout>(R.id.contact_with_support).setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.data = Uri.parse("mailto:" + this.getString(R.string.email_support_address))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.email_support_title))
            emailIntent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.email_support_body))
            startActivity(emailIntent)
        }

        findViewById<FrameLayout>(R.id.user_agreement).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.user_agreement_link)))
            startActivity(browserIntent)
        }
    }
}