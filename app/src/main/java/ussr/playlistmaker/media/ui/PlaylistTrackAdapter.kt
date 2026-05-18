package ussr.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.databinding.ListItemPlaylistCardBinding
import ussr.playlistmaker.playlist.data.models.PlaylistModel

class PlaylistTrackAdapter: RecyclerView.Adapter<PlaylistTrackViewHolder>() {
    private val playlists = mutableListOf<PlaylistModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistTrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlaylistCardBinding.inflate(inflater, parent,false)
        return PlaylistTrackViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistTrackViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun setList(items: List<PlaylistModel>){
        playlists.clear()
        playlists.addAll(items)
        notifyDataSetChanged()
    }
}