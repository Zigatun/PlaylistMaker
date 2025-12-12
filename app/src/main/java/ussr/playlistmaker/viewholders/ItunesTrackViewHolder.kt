package ussr.playlistmaker.viewholders

import android.content.Intent
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ussr.playlistmaker.MediaActivity
import ussr.playlistmaker.PlayerActivity
import ussr.playlistmaker.R
import ussr.playlistmaker.models.ItunesTrack
import java.text.SimpleDateFormat
import java.util.Locale

class ItunesTrackViewHolder(private val parentView: View): RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView = parentView.findViewById(R.id.trackName)
    private val trackAuthor: TextView = parentView.findViewById(R.id.trackAuthor)
    private val trackDuration: TextView = parentView.findViewById(R.id.trackDuration)
    private val albumCover: ImageView= parentView.findViewById(R.id.albumImage)

    fun bind(model: ItunesTrack){
        trackName.text = model.trackName
        trackAuthor.text = model.artistName
        trackDuration.text = model.getHumanizedTime()
        val radius = 2f
        val metrics: DisplayMetrics = parentView.resources.displayMetrics
        val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics)
        Glide.with(parentView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusPx.toInt())))
            .into(albumCover)

    }
}