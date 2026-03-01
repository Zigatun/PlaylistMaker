package ussr.playlistmaker.search.ui

import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ActivityTrackCardBinding
import ussr.playlistmaker.search.models.Track

class ItunesTrackViewHolder(private val binding: ActivityTrackCardBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Track){
        binding.trackName.text = model.trackName
        binding.trackAuthor.text = model.artistName
        binding.trackDuration.text = model.trackTime
        val radius = 2f
        val metrics: DisplayMetrics = binding.root.resources.displayMetrics
        val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics)
        Glide.with(binding.root)
            .load(model.coverArtworkUrl)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusPx.toInt())))
            .into(binding.albumImage)
    }
}