package ussr.playlistmaker.media.ui

import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ListItemPlaylistCardBinding
import ussr.playlistmaker.playlist.data.models.PlaylistModel

class PlaylistTrackViewHolder(private val binding: ListItemPlaylistCardBinding): RecyclerView.ViewHolder(binding.root) {
    private fun formatTrackCount(count: Int): String = when {
            count == 0 -> "Плейлист пуст"
            count % 100 in 11..14 -> "$count треков"
            count % 10 == 1 -> "$count трек"
            count % 10 in 2..4 -> "$count трека"
            else ->  "$count треков"
        }

    fun bind(target: PlaylistModel){
        binding.playlistName.text = target.title
        binding.playlistTracksCount.text = formatTrackCount(target.content.size)
        val radius = 2f
        val metrics: DisplayMetrics = binding.root.resources.displayMetrics
        val radiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics)
        Glide.with(binding.root)
            .load(target.imagePath)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusPx.toInt())))
            .into(binding.playlistImage)
    }
}