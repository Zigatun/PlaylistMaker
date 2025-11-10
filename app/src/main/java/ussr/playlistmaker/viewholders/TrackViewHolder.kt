package ussr.playlistmaker.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ussr.playlistmaker.R
import ussr.playlistmaker.models.Track

class TrackViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView = parentView.findViewById(R.id.trackName)
    private val trackAuthor: TextView = parentView.findViewById(R.id.trackAuthor)
    private val trackDuration: TextView = parentView.findViewById(R.id.trackDuration)
    private val albumCover: ImageView= parentView.findViewById(R.id.albumImage)

    fun bind(model: Track){
        trackName.text = model.trackName
        trackAuthor.text = model.artistName
        trackDuration.text = model.trackTime
        Glide.with(parentView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .into(albumCover)
    }
}