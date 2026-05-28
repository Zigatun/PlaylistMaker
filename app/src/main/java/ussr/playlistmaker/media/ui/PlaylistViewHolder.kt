package ussr.playlistmaker.media.ui

import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ListItemPlaylistCardBinding
import ussr.playlistmaker.playlist.data.models.PlaylistModel

class PlaylistViewHolder(private val binding: ListItemPlaylistCardBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(target: PlaylistModel){
        binding.playlistName.text = target.title
        binding.playlistTracksCount.text = target.formatTrackCount()
        val radius = 2f
        val metrics: DisplayMetrics = binding.root.resources.displayMetrics
        val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics)
        Glide.with(binding.root)
            .load(target.imagePath)
            .placeholder(R.drawable.placeholder_image)
            .transform(
                CenterCrop(),
                RoundedCorners(radiusPx.toInt())
            )
            .into(binding.playlistImage)
    }
}